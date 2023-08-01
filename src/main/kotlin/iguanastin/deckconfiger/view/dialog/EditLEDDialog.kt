package iguanastin.deckconfiger.view.dialog

import iguanastin.deckconfiger.app.Styles
import iguanastin.deckconfiger.app.config.hardware.HardwareDefinition
import iguanastin.deckconfiger.app.config.hardware.LEDLight
import iguanastin.deckconfiger.view.onActionConsuming
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import tornadofx.*

class EditLEDDialog(led: LEDLight? = null, hw: HardwareDefinition, onAccept: (LEDLight) -> Unit = {}, onClose: () -> Unit = {}): StackDialog(onClose) {

    private lateinit var nameField: TextField
    private lateinit var pinField: TextField
    private lateinit var acceptButton: Button
    private lateinit var cancelButton: Button

    init {
        root.graphic = vbox(10) {
            addClass(Styles.dialogRoot)
            label(if (led == null) "New LED" else "Edit LED") { addClass(Styles.dialogHeader) }

            hbox(5) {
                alignment = Pos.CENTER_LEFT
                label("Name:")
                nameField = textfield(led?.name) {
                    promptText = "Name"
                }
            }
            hbox(5) {
                alignment = Pos.CENTER_LEFT
                label("Pin:")
                pinField = textfield(led?.primaryPin?.toString()) {
                    promptText = "Pin"
                    textFormatter = TextFormatter<String> {
                        if (it.controlNewText.matches("[0-9]*".toRegex())) it else null
                    }
                    onActionConsuming { acceptButton.fire() }
                }
            }
            hbox {
                alignment = Pos.CENTER_RIGHT
                acceptButton = button("Ok") {
                    onActionConsuming {
                        if (pinField.text?.toIntOrNull() == null) {
                            pinField.addClass(Styles.redBG)
                            return@onActionConsuming
                        }
                        close()
                        val pin = pinField.text.toInt()
                        var result = led
                        if (result == null) result = LEDLight(id = hw.getNextID())
                        result.name = nameField.text
                        result.primaryPin = pin
                        onAccept(result)
                    }
                }
                cancelButton = button("Cancel") {
                    onActionConsuming { close() }
                }
            }
        }
    }

    override fun requestFocus() {
        pinField.requestFocus()
    }

}