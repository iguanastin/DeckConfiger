package iguanastin.deckconfiger.view

import iguanastin.deckconfiger.app.config.DeckConfig
import iguanastin.deckconfiger.app.config.hardware.HardwareComponent
import iguanastin.deckconfiger.app.config.profile.DeckProfile
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventTarget
import javafx.scene.Node
import javafx.scene.control.Label
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

            content = pane {
                var componentsListener: ListConversionListener<HardwareComponent, Node>? = null

                deckConfigProperty.addListener { _, oldDeck, newDeck ->
                    oldDeck?.hardware?.components?.removeListener(componentsListener)
                    children.clear()
                    if (newDeck == null) return@addListener

                    componentsListener = children.bind(newDeck.hardware.components) {
                        Label(it.type) // TODO not working and not exactly what I wanted either
                    }
                }
            }
        }

        anchorpane {
            isPickOnBounds = false

            // Toggle hardware editing button
            togglebutton {
                anchorpaneConstraints {
                    leftAnchor = 10
                    bottomAnchor = 10
                }
                enableWhen(deckConfigProperty.isNotNull)
                textProperty().bind(selectedProperty().map { if (it) "Lock Hardware" else "Unlock Hardware" })
                selectedProperty().bindBidirectional(editHardwareProperty)
            }

            // Profile chooser
            choicebox<DeckProfile> {
                anchorpaneConstraints {
                    leftAnchor = 10
                    topAnchor = 10
                }
                enableWhen(deckConfigProperty.isNotNull)

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
        }
    }

}


fun EventTarget.configeditor(op: ConfigEditor.() -> Unit = {}) = opcr(this, ConfigEditor(), op)