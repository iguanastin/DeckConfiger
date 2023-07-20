package iguanastin.deckconfiger.view

import iguanastin.deckconfiger.app.Styles
import iguanastin.deckconfiger.app.config.DeckConfig
import iguanastin.deckconfiger.app.config.hardware.HardwareComponent
import iguanastin.deckconfiger.app.config.hardware.LEDLight
import iguanastin.deckconfiger.app.config.hardware.PushButton
import iguanastin.deckconfiger.app.config.hardware.RotaryEncoder
import iguanastin.deckconfiger.app.config.profile.DeckProfile
import iguanastin.deckconfiger.view.components.LEDLightView
import iguanastin.deckconfiger.view.components.PushButtonView
import iguanastin.deckconfiger.view.components.RotaryEncoderView
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Modality
import javafx.util.Callback
import tornadofx.*

class ConfigEditor : StackPane() {

    val deckConfigProperty = SimpleObjectProperty<DeckConfig?>()
    var deckConfig: DeckConfig?
        get() = deckConfigProperty.get()
        set(value) = deckConfigProperty.set(value)

    val profileProperty = SimpleObjectProperty<DeckProfile>()
    var profile: DeckProfile?
        get() = profileProperty.get()
        set(value) = profileProperty.set(value)

    val editHardwareProperty = SimpleBooleanProperty(false)
    var editHardware: Boolean
        get() = editHardwareProperty.get()
        set(value) = editHardwareProperty.set(value)


    init {
        deckConfigProperty.addListener { _, _, new ->
            if (new?.profiles?.contains(profile) != true) profile = new?.profiles?.firstOrNull()
        }

        // Main content pane
        scrollpane {
            isFitToWidth = true
            isFitToHeight = true
            isPannable = true

            content = borderpane {
                padding = insets(50)

                center = group {
                    var componentsListener: ListConversionListener<HardwareComponent, Node>? = null

                    deckConfigProperty.addListener { _, oldDeck, newDeck ->
                        oldDeck?.hardware?.components?.removeListener(componentsListener)
                        children.clear()
                        if (newDeck == null) return@addListener

                        componentsListener = children.bind(newDeck.hardware.components) {
                            val view = when (it) {
                                is LEDLight -> LEDLightView(it)
                                is PushButton -> PushButtonView(it)
                                is RotaryEncoder -> RotaryEncoderView(it)
                                else -> throw IllegalArgumentException("Invalid type: $it")
                            }

                            view.draggableProperty.bind(editHardwareProperty)
                            view.contextmenu {
                                item("Edit") {
                                    enableWhen(editHardwareProperty)
                                    onAction = EventHandler { event ->
                                        event.consume()
                                        // TODO
                                    }
                                }
                                item("Delete") {
                                    enableWhen(editHardwareProperty)
                                    onAction = EventHandler { event ->
                                        event.consume()
                                        confirm(
                                            "Delete this component?",
                                            confirmButton = ButtonType.YES,
                                            cancelButton = ButtonType.NO,
                                            owner = ownerWindow,
                                            title = "Delete?"
                                        ) {
                                            deckConfig?.hardware?.components?.remove(it)
                                        }
                                    }
                                }
                            }

                            view
                        }
                    }
                }

                contextmenu {
                    item("New Component") {
                        enableWhen(editHardwareProperty)
                        onAction = EventHandler { event ->
                            event.consume()
                            // TODO
                        }
                    }
                }
            }
        }

        anchorpane {
            isPickOnBounds = false
            enableWhen(deckConfigProperty.isNotNull)

            // Toggle hardware editing button
            togglebutton {
                anchorpaneConstraints {
                    leftAnchor = 10
                    bottomAnchor = 10
                }
                textProperty().bind(selectedProperty().map { if (it) "Lock Hardware" else "Unlock Hardware" })
                selectedProperty().bindBidirectional(editHardwareProperty)
            }

            hbox(5.0) {
                anchorpaneConstraints {
                    leftAnchor = 10
                    topAnchor = 10
                }

                // Profile chooser
                choicebox<DeckProfile> {
                    var profilesListener: ListConversionListener<DeckProfile, DeckProfile>? = null
                    deckConfigProperty.addListener { _, old, new ->
                        old?.profiles?.removeListener(profilesListener)
                        items.clear()
                        if (new == null) return@addListener

                        profilesListener = items.bind(new.profiles) { it }
                        value = items.firstOrNull()
                    }

                    valueProperty().bindBidirectional(profileProperty)
                }
                button("\uD83D\uDD89") {
                    tooltip("Edit Profile")
                    onAction = EventHandler { event ->
                        event.consume()
                        profileDialog(profile)
                    }
                }
                button("➕") {
                    tooltip("New Profile")
                    onAction = EventHandler { event ->
                        event.consume()
                        val result = profileDialog() ?: return@EventHandler
                        deckConfig?.profiles?.add(result)
                        this@ConfigEditor.profile = result
                    }
                }
                button("\uD83D\uDDD1") {
                    deckConfigProperty.addListener { _, _, new ->
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
                            deckConfig?.profiles?.remove(profile)
                            profile = deckConfig?.profiles?.firstOrNull()
                        }
                    }
                }
            }
        }
    }

    private fun profileDialog(profile: DeckProfile? = null): DeckProfile? {
        Dialog<DeckProfile>().apply {
            initModality(Modality.WINDOW_MODAL)
            initOwner(scene.window)
            title = if (profile == null) "New Profile" else "Edit Profile"

            val nameField = textfield {
                promptText = "Profile name"
                if (profile != null) text = profile.name
            }
            val colorField = textfield {
                promptText = "Profile Color"
                if (profile != null) text = profile.color
            }
            val patternField = textfield {
                promptText = "N/A"
                if (profile != null) text = profile.pattern
            }

            dialogPane.apply {
                this.graphic = vbox(10) {
                    hbox(5) {
                        label("Name")
                        add(nameField)
                    }
                    hbox(5) {
                        label("Color")
                        add(colorField)
                    }
                    hbox(5) {
                        label("Pattern")
                        add(patternField)
                    }
                }
                buttonTypes.add(ButtonType.APPLY)
                lookupButton(ButtonType.APPLY).addEventFilter(ActionEvent.ACTION) { event ->
                    if (nameField.text.isBlank() || deckConfig?.profiles?.map { it.name }?.contains(nameField.text.trim()) == true) {
                        event.consume()
                        nameField.addClass(Styles.redBG)
                    }
                }
            }

            resultConverter = Callback {
                if (it != ButtonType.APPLY) return@Callback null

                var result = profile
                if (result == null) {
                    result = DeckProfile(nameField.text, colorField.text, patternField.text)
                } else {
                    result.name = nameField.text
                    result.color = colorField.text
                    result.pattern = patternField.text
                    if (result.pattern?.isBlank() == true) result.pattern = null
                }

                return@Callback result
            }

            return showAndWait().orElse(null)
        }
    }

}