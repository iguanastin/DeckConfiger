package iguanastin.deckconfiger.view

import iguanastin.deckconfiger.app.MyApp
import iguanastin.deckconfiger.app.config.hardware.*
import iguanastin.deckconfiger.app.config.profile.DeckProfile
import iguanastin.deckconfiger.view.components.*
import iguanastin.deckconfiger.view.dialog.*
import javafx.beans.property.SimpleBooleanProperty
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.ButtonType
import javafx.scene.control.ContextMenu
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import tornadofx.*

class ConfigEditor(private val app: MyApp) : StackPane() {

    val editHardwareProperty = SimpleBooleanProperty(false)
    var editHardware by editHardwareProperty


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
                    Menu(
                        "New Component",
                        null,
                        MenuItem("Button").apply {
                            onActionConsuming {
                                openNewButtonDialog()
                            }
                        },
                        MenuItem("Encoder").apply {
                            onActionConsuming {
                                openNewEncoderDialog()
                            }
                        },
                        MenuItem("LED Light").apply {
                            onActionConsuming {
                                openNewLEDDialog()
                            }
                        },
                        MenuItem("RGB Light").apply {
                            onActionConsuming {
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

    private fun initComponentView(component: HardwareComponent): HardwareComponentView {
        val view = when (component) {
            is LEDLight -> LEDLightView(component)
            is RGBLight -> RGBLightView(component)
            is Button -> ButtonView(component)
            is Encoder -> EncoderView(component)
            else -> throw IllegalArgumentException("Invalid type: $component")
        }

        view.onLeftClick {
            if (component.id !in app.profile!!.bindingByID) app.profile!!.bindings.add(component.createBinding())
            app.dialog(BindingDialog(app.profile!!.bindingByID[component.id]!!, app))
        }

        view.draggableProperty.bind(editHardwareProperty)
        view.contextmenu {
            item("Edit") {
                visibleWhen(editHardwareProperty)
                onActionConsuming {
                    runLater {
                        when (component) {
                            is LEDLight -> editLEDDialog(component)
                            is RGBLight -> editRGBDialog(component)
                            is Button -> editButtonDialog(component)
                            is Encoder -> editEncoderDialog(component)
                            else -> throw IllegalArgumentException("Invalid type: $component")
                        }
                    }
                }
            }
            item("Delete") {
                visibleWhen(editHardwareProperty)
                onActionConsuming {
                    confirm(
                        "Delete this component?",
                        confirmButton = ButtonType.YES,
                        cancelButton = ButtonType.NO,
                        owner = ownerWindow,
                        title = "Delete?"
                    ) {
                        app.deckConfig?.hardware?.components?.remove(component)
                    }
                }
            }
            if (component is LEDLight) item("Ident") {
                onActionConsuming {
                    app.identLED(component.primaryPin)
                    (view as LEDLightView).ident()
                }
            }
            if (component is RGBLight) item("Ident") {
                onActionConsuming {
                    app.identRGB(component)
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
                onActionConsuming { editProfileDialog(app.profile) }
            }
            button("➕") {
                tooltip("New Profile")
                onActionConsuming {
                    val profile = DeckProfile("New profile")
                    editProfileDialog(profile).onAccept = {
                        app.deckConfig?.profiles?.add(profile)
                        app.profile = profile
                    }
                }
            }
            button("\uD83D\uDDD1") {
                app.deckConfigProperty.addListener { _, _, new ->
                    if (new != null) enableWhen(new.profiles.sizeProperty.greaterThan(1))
                    else disableProperty().unbind()
                }
                tooltip("Delete Profile")
                onActionConsuming {
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

    fun editButtonDialog(button: Button): EditDialog {
        return EditDialog("Edit Button", listOf(
            EditStringField("Name", button.nameProperty),
            EditIntField("Pin", button.primaryPinProperty),
            EditIntField("Debounce", button.debounceProperty),
            EditChoiceField("Detect", button.detectPressProperty, *Button.Detect.values()),
        )).also { app.dialog(it) }
    }

    fun editEncoderDialog(enc: Encoder): EditDialog {
        return EditDialog("Edit Encoder", listOf(
            EditStringField("Name", enc.nameProperty),
            EditIntField("Pin 1", enc.primaryPinProperty),
            EditIntField("Pin 2", enc.secondaryPinProperty)
        )).also { app.dialog(it) }
    }

    fun editLEDDialog(led: LEDLight): EditDialog {
        return EditDialog("Edit LED", listOf(
            EditStringField("Name", led.nameProperty),
            EditIntField("Pin", led.primaryPinProperty)
        )).also { app.dialog(it) }
    }

    fun editRGBDialog(led: RGBLight): EditDialog {
        return EditDialog("Edit RGB LED", listOf(
            EditStringField("Name", led.nameProperty),
            EditIntField("R Pin", led.primaryPinProperty),
            EditIntField("G Pin", led.gPinProperty),
            EditIntField("B Pin", led.bPinProperty),
            EditIntField("R", led.rProperty, min = 0, max = 255),
            EditIntField("G", led.gProperty, min = 0, max = 255),
            EditIntField("B", led.bProperty, min = 0, max = 255),
        )).also { app.dialog(it) }
    }

    fun editProfileDialog(profile: DeckProfile): EditDialog {
        val colorProp = objectProperty(Color.color(
            profile.r / 255.0,
            profile.g / 255.0,
            profile.b / 255.0
        ))
        return EditDialog("Edit Profile", listOf(
            EditStringField("Name", profile.nameProperty),
            EditColorField("Color", colorProp)
        )).apply {
            acceptListeners.add {
                profile.r = (colorProp.value.red * 255).toInt()
                profile.g = (colorProp.value.green * 255).toInt()
                profile.b = (colorProp.value.blue * 255).toInt()
            }
        }.also { app.dialog(it) }
    }

    fun openNewLEDDialog() {
        val new = LEDLight()
        editLEDDialog(new).onAccept = {
            new.id = app.deckConfig!!.hardware.getNextID()
            app.deckConfig!!.hardware.components.add(new)
        }
    }

    fun openNewRGBDialog() {
        val new = RGBLight()
        editRGBDialog(new).onAccept = {
            new.id = app.deckConfig!!.hardware.getNextID()
            app.deckConfig!!.hardware.components.add(new)
        }
    }

    fun openNewEncoderDialog() {
        val new = Encoder()
        editEncoderDialog(new).onAccept = {
            new.id = app.deckConfig!!.hardware.getNextID()
            app.deckConfig!!.hardware.components.add(new)
        }
    }

    fun openNewButtonDialog() {
        val new = Button()
        editButtonDialog(new).onAccept = {
            new.id = app.deckConfig!!.hardware.getNextID()
            app.deckConfig!!.hardware.components.add(new)
        }
    }

}