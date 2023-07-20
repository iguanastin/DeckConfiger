package iguanastin.deckconfiger.view

import iguanastin.deckconfiger.app.config.DeckConfig
import iguanastin.deckconfiger.app.config.hardware.HardwareComponent
import iguanastin.deckconfiger.app.config.hardware.LEDLight
import iguanastin.deckconfiger.app.config.hardware.PushButton
import iguanastin.deckconfiger.app.config.hardware.RotaryEncoder
import iguanastin.deckconfiger.app.config.profile.DeckProfile
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.layout.StackPane
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

                            view
                        }
                    }
                }

                contextmenu {
                    item("No Actions...") {
                        isDisable = true
                        visibleWhen(editHardwareProperty.not()) // DO NOT invert this because the warning is flat out wrong. Will hide the whole content pane
                    }
                    item("New Component") {
                        visibleWhen(editHardwareProperty)
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

                        profilesListener = items.bind(new.profiles) {
                            it
                        }
                        value = items.firstOrNull()
                    }

                    valueProperty().bindBidirectional(profileProperty)
                }
                button("+") {
                    onAction = EventHandler { event ->
                        event.consume()
                        // TODO create new profile
                    }
                }
            }
        }
    }

}