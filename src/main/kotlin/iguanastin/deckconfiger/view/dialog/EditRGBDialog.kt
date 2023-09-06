package iguanastin.deckconfiger.view.dialog

import iguanastin.deckconfiger.app.Styles
import iguanastin.deckconfiger.app.config.hardware.HardwareDefinition
import iguanastin.deckconfiger.app.config.hardware.RGBLight
import iguanastin.deckconfiger.view.onActionConsuming
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import tornadofx.*

class EditRGBDialog(
    led: RGBLight? = null,
    hw: HardwareDefinition,
    onAccept: (RGBLight) -> Unit = {},
    onClose: () -> Unit = {}
) : StackDialog(onClose) {

    private lateinit var nameField: TextField
    private lateinit var rPinField: TextField
    private lateinit var gPinField: TextField
    private lateinit var bPinField: TextField
    private lateinit var rField: TextField
    private lateinit var gField: TextField
    private lateinit var bField: TextField
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
                label("R Pin:")
                rPinField = textfield(led?.primaryPin?.toString()) {
                    promptText = "Pin"
                    textFormatter = TextFormatter<String> {
                        if (it.controlNewText.matches("[0-9]*".toRegex())) it else null
                    }
                    onActionConsuming { gPinField.requestFocus() }
                }
            }
            hbox(5) {
                alignment = Pos.CENTER_LEFT
                label("G Pin:")
                gPinField = textfield(led?.gPin?.toString()) {
                    promptText = "Pin"
                    textFormatter = TextFormatter<String> {
                        if (it.controlNewText.matches("[0-9]*".toRegex())) it else null
                    }
                    onActionConsuming { bPinField.requestFocus() }
                }
            }
            hbox(5) {
                alignment = Pos.CENTER_LEFT
                label("B Pin:")
                bPinField = textfield(led?.bPin?.toString()) {
                    promptText = "Pin"
                    textFormatter = TextFormatter<String> {
                        if (it.controlNewText.matches("[0-9]*".toRegex())) it else null
                    }
                    onActionConsuming { rField.requestFocus() }
                }
            }
            hbox(5) {
                alignment = Pos.CENTER_LEFT
                label("R:")
                rField = textfield(led?.r?.toString()) {
                    promptText = "0-256"
                    textFormatter = TextFormatter<String> {
                        if (it.controlNewText.matches("[0-9]*".toRegex()) && (it.controlNewText.toIntOrNull() == null || it.controlNewText.toInt() in 0 until 256)) it else null
                    }
                    onActionConsuming { gField.requestFocus() }
                }
            }
            hbox(5) {
                alignment = Pos.CENTER_LEFT
                label("G:")
                gField = textfield(led?.g?.toString()) {
                    promptText = "0-256"
                    textFormatter = TextFormatter<String> {
                        if (it.controlNewText.matches("[0-9]*".toRegex()) && (it.controlNewText.toIntOrNull() == null || it.controlNewText.toInt() in 0 until 256)) it else null
                    }
                    onActionConsuming { bField.requestFocus() }
                }
            }
            hbox(5) {
                alignment = Pos.CENTER_LEFT
                label("B:")
                bField = textfield(led?.b?.toString()) {
                    promptText = "0-256"
                    textFormatter = TextFormatter<String> {
                        if (it.controlNewText.matches("[0-9]*".toRegex()) && (it.controlNewText.toIntOrNull() == null || it.controlNewText.toInt() in 0 until 256)) it else null
                    }
                    onActionConsuming { acceptButton.fire() }
                }
            }
            hbox {
                alignment = Pos.CENTER_RIGHT
                acceptButton = button("Ok") {
                    onActionConsuming {
                        if (rPinField.text?.toIntOrNull() == null) {
                            rPinField.addClass(Styles.redBG)
                            return@onActionConsuming
                        }
                        close()
                        var result = led
                        if (result == null) result = RGBLight(id = hw.getNextID())
                        result.name = nameField.text
                        result.primaryPin = rPinField.text.toInt()
                        result.gPin = gPinField.text.toInt()
                        result.bPin = bPinField.text.toInt()
                        result.r = rField.text.toInt()
                        result.g = gField.text.toInt()
                        result.b = bField.text.toInt()
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
        rPinField.requestFocus()
    }

}