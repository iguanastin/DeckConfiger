package iguanastin.deckconfiger.view

import iguanastin.deckconfiger.app.MyApp
import iguanastin.deckconfiger.app.addIfNotContains
import iguanastin.deckconfiger.app.config.DeckConfig
import iguanastin.deckconfiger.app.config.hardware.*
import iguanastin.deckconfiger.app.config.profile.DeckProfile
import iguanastin.deckconfiger.view.components.*
import iguanastin.deckconfiger.view.dialog.*
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.ButtonType
import javafx.scene.control.ContextMenu
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.StackPane
import tornadofx.*

class ConfigEditor(private val app: MyApp) : StackPane() {

    val editHardwareProperty = SimpleBooleanProperty(false)
    var editHardware: Boolean
        get() = editHardwareProperty.get()
        set(value) = editHardwareProperty.set(value)


    init {
        app.deckConfigProperty.addListener { _, _, new ->
            if (new?.profiles?.contains(app.profile) != true) app.profile = new?.profiles?.firstOrNull()
        }

        // Main content pane
        scrollpane {
            isFitToWidth = true
            isFitToHeight = true
            isPannable = true

            content = borderpane {
                padding = insets(50)

                // Component view group
                center = group {
                    var componentsListener: ListConversionListener<HardwareComponent, Node>? = null

                    app.deckConfigProperty.addListener { _, oldDeck, newDeck ->
                        oldDeck?.hardware?.components?.removeListener(componentsListener)
                        children.clear()
                        if (newDeck == null) return@addListener

                        componentsListener = children.bind(newDeck.hardware.components) { initComponentView(it) }
                    }
                }

                // Hardware editing contextmenu
                val cm = ContextMenu(
                    Menu("New Component",
                        null,
                        MenuItem("Button").apply {
                            onAction = EventHandler { event ->
                                event.consume()
                                openNewButtonDialog()
                            }
                        },
                        MenuItem("Encoder").apply {
                            onAction = EventHandler { event ->
                                event.consume()
                                openNewEncoderDialog()
                            }
                        },
                        MenuItem("LED Light").apply {
                            onAction = EventHandler { event ->
                                event.consume()
                                openNewLEDDialog()
                            }
                        },
                        MenuItem("RGB Light").apply {
                            onAction = EventHandler { event ->
                                event.consume()
                                openNewRGBDialog()
                            }
                        })
                )
                contextMenu = null
                editHardwareProperty.addListener { _, _, new ->
                    contextMenu = if (new) cm else null
                }
            }
        }

        anchorpane {
            isPickOnBounds = false
            enableWhen(app.deckConfigProperty.isNotNull)

            initProfileControls()

            borderpane {
                isPickOnBounds = false
                anchorpaneConstraints {
                    bottomAnchor = 10
                    leftAnchor = 10
                }

                // TODO popup binding info panel
            }
        }
    }

    private fun initComponentView(it: HardwareComponent): HardwareComponentView {
        val view = when (it) {
            is LEDLight -> LEDLightView(it)
            is RGBLight -> RGBLightView(it)
            is Button -> ButtonView(it)
            is Encoder -> EncoderView(it)
            else -> throw IllegalArgumentException("Invalid type: $it")
        }

        // TODO click to open binding editor

        view.draggableProperty.bind(editHardwareProperty)
        view.contextmenu {
            item("Edit") {
                visibleWhen(editHardwareProperty)
                onAction = EventHandler { event ->
                    event.consume()
                    runLater {
                        when (it) {
                            is LEDLight -> app.root.root.add(EditLEDDialog(it, app.deckConfig!!.hardware))
                            is RGBLight -> app.root.root.add(EditRGBDialog(it, app.deckConfig!!.hardware))
                            is Button -> app.root.root.add(EditButtonDialog(it, app.deckConfig!!.hardware))
                            is Encoder -> app.root.root.add(EditEncoderDialog(it, app.deckConfig!!.hardware))
                            else -> throw IllegalArgumentException("Invalid type: $it")
                        }
                    }
                }
            }
            item("Delete") {
                visibleWhen(editHardwareProperty)
                onAction = EventHandler { event ->
                    event.consume()
                    confirm(
                        "Delete this component?",
                        confirmButton = ButtonType.YES,
                        cancelButton = ButtonType.NO,
                        owner = ownerWindow,
                        title = "Delete?"
                    ) {
                        app.deckConfig?.hardware?.components?.remove(it)
                    }
                }
            }
            if (it is LEDLight) item("Ident") {
                onAction = EventHandler { event ->
                    event.consume()
                    app.identLED(it.primaryPin)
                    (view as LEDLightView).ident()
                }
            }
            if (it is RGBLight) item("Ident") {
                onAction = EventHandler { event ->
                    event.consume()
                    app.identRGB(it)
                    (view as RGBLightView).ident()
                }
            } else {
                item("No actions") {
                    this.visibleWhen(editHardwareProperty.not())
                    isDisable = true
                }
            }
        }
        return view
    }

    private fun AnchorPane.initProfileControls() {
        hbox(5.0) {
            anchorpaneConstraints {
                leftAnchor = 10
                topAnchor = 10
            }

            // Profile chooser
            choicebox<DeckProfile> {
                var profilesListener: ListConversionListener<DeckProfile, DeckProfile>? = null
                app.deckConfigProperty.addListener { _, old, new ->
                    old?.profiles?.removeListener(profilesListener)
                    items.clear()
                    if (new == null) return@addListener

                    profilesListener = items.bind(new.profiles) { it }
                    value = items.firstOrNull()
                }

                valueProperty().bindBidirectional(app.profileProperty)
            }
            button("\uD83D\uDD89") {
                tooltip("Edit Profile")
                onAction = EventHandler { event ->
                    event.consume()
                    app.root.root.add(EditProfileDialog(app.profile))
                }
            }
            button("➕") {
                tooltip("New Profile")
                onAction = EventHandler { event ->
                    event.consume()
                    app.root.root.add(EditProfileDialog(onAccept = {
                        app.deckConfig?.profiles?.add(it)
                        app.profile = it
                    }))
                }
            }
            button("\uD83D\uDDD1") {
                app.deckConfigProperty.addListener { _, _, new ->
                    if (new != null) enableWhen(new.profiles.sizeProperty.greaterThan(1))
                    else disableProperty().unbind()
                }
                tooltip("Delete Profile")
                onAction = EventHandler { event ->
                    event.consume()
                    confirm(
                        "Delete this profile?",
                        confirmButton = ButtonType.YES,
                        cancelButton = ButtonType.NO,
                        owner = scene.window,
                        title = "Delete?"
                    ) {
                        app.deckConfig?.profiles?.remove(app.profile)
                        app.profile = app.deckConfig?.profiles?.firstOrNull()
                    }
                }
            }
        }
    }

    fun openNewLEDDialog() {
        app.root.root.add(EditLEDDialog(null, app.deckConfig?.hardware ?: return, onAccept = {
            app.deckConfig?.hardware?.components?.addIfNotContains(it)
        }))
    }

    fun openNewRGBDialog() {
        app.root.root.add(EditRGBDialog(null, app.deckConfig?.hardware ?: return, onAccept = {
            app.deckConfig?.hardware?.components?.addIfNotContains(it)
        }))
    }

    fun openNewEncoderDialog() {
        app.root.root.add(EditEncoderDialog(null, app.deckConfig?.hardware ?: return, onAccept = {
            app.deckConfig?.hardware?.components?.addIfNotContains(it)
        }))
    }

    fun openNewButtonDialog() {
        app.root.root.add(EditButtonDialog(null, app.deckConfig?.hardware ?: return, onAccept = {
            app.deckConfig?.hardware?.components?.addIfNotContains(it)
        }))
    }

}