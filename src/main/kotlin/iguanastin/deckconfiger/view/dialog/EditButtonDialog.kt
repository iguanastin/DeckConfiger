package iguanastin.deckconfiger.view.dialog

import iguanastin.deckconfiger.app.Styles
import iguanastin.deckconfiger.app.config.hardware.Button
import iguanastin.deckconfiger.app.config.hardware.HardwareDefinition
import iguanastin.deckconfiger.view.onActionConsuming
import javafx.geometry.Pos
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.util.StringConverter
import tornadofx.*

class EditButtonDialog(
    button: Button? = null,
    hw: HardwareDefinition,
    onAccept: (Button) -> Unit = {},
    onClose: () -> Unit = {}
) : StackDialog(onClose) {

    private lateinit var pinField: TextField
    private lateinit var debounceField: TextField
    private lateinit var detectField: ChoiceBox<Int>
    private lateinit var acceptButton: javafx.scene.control.Button
    private lateinit var cancelButton: javafx.scene.control.Button

    init {
        root.graphic = vbox(10) {
            addClass(Styles.dialogRoot)
            label(if (button == null) "New button" else "Edit button") { addClass(Styles.dialogHeader) }
            hbox(5) {
                alignment = Pos.CENTER_LEFT
                label("Pin:")
                pinField = textfield(button?.primaryPin?.toString()) {
                    promptText = "Pin"
                    textFormatter = TextFormatter<String> {
                        if (it.controlNewText.matches("[0-9]*".toRegex())) it else null
                    }
                }
            }
            hbox(5) {
                alignment = Pos.CENTER_LEFT
                label("Debounce:")
                debounceField = textfield(button?.debounceInterval?.toString()) {
                    promptText = "Milliseconds"
                    textFormatter = TextFormatter<String> {
                        if (it.controlNewText.matches("[0-9]*".toRegex())) it else null
                    }
                }
            }
            hbox(5) {
                alignment = Pos.CENTER_LEFT
                label("Detect:")
                detectField = choicebox {
                    items.addAll(1, 0)
                    value = button?.detectPress ?: 0
                    converter = object : StringConverter<Int?>() {
                        override fun toString(int: Int?): String {
                            return if (int == 0) "LOW" else "HIGH"
                        }

                        override fun fromString(string: String?): Int? {
                            return if (string == "LOW") 0 else 1
                        }
                    }
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
                        var result = button
                        if (result == null) result = Button(hw.getNextID(), 0, 0, 0)
                        result.primaryPin = pinField.text.toInt()
                        result.detectPress = detectField.value
                        result.debounceInterval = debounceField.text.toInt()
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