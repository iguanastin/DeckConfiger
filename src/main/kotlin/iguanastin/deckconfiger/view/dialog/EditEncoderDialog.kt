package iguanastin.deckconfiger.view.dialog

import iguanastin.deckconfiger.app.Styles
import iguanastin.deckconfiger.app.config.hardware.Encoder
import iguanastin.deckconfiger.app.config.hardware.HardwareDefinition
import iguanastin.deckconfiger.view.onActionConsuming
import javafx.geometry.Pos
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import tornadofx.*

class EditEncoderDialog(
    encoder: Encoder? = null,
    hw: HardwareDefinition,
    onAccept: (Encoder) -> Unit = {},
    onClose: () -> Unit = {}
) : StackDialog(onClose) {

    private lateinit var pin1Field: TextField
    private lateinit var pin2Field: TextField
    private lateinit var acceptButton: javafx.scene.control.Button
    private lateinit var cancelButton: javafx.scene.control.Button

    init {
        root.graphic = vbox(10) {
            addClass(Styles.dialogRoot)
            label(if (encoder == null) "New encoder" else "Edit encoder") { addClass(Styles.dialogHeader) }
            hbox(5) {
                alignment = Pos.CENTER_LEFT
                label("Pin 1:")
                pin1Field = textfield(encoder?.primaryPin?.toString()) {
                    promptText = "Pin"
                    textFormatter = TextFormatter<String> {
                        if (it.controlNewText.matches("[0-9]*".toRegex())) it else null
                    }
                }
            }
            hbox(5) {
                alignment = Pos.CENTER_LEFT
                label("Pin 2:")
                pin2Field = textfield(encoder?.secondaryPin?.toString()) {
                    promptText = "Pin"
                    textFormatter = TextFormatter<String> {
                        if (it.controlNewText.matches("[0-9]*".toRegex())) it else null
                    }
                }
            }
            hbox {
                alignment = Pos.CENTER_RIGHT
                acceptButton = button("Ok") {
                    onActionConsuming {
                        if (pin1Field.text?.toIntOrNull() == null) {
                            pin1Field.addClass(Styles.redBG)
                            return@onActionConsuming
                        }
                        if (pin2Field.text?.toIntOrNull() == null) {
                            pin2Field.addClass(Styles.redBG)
                            return@onActionConsuming
                        }
                        close()
                        var result = encoder
                        if (result == null) result = Encoder(hw.getNextID(), 0, 0, 0, 0)
                        result.primaryPin = pin1Field.text.toInt()
                        result.secondaryPin = pin2Field.text.toInt()
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
        pin1Field.requestFocus()
    }

}