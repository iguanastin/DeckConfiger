package iguanastin.deckconfiger.view.dialog

import iguanastin.deckconfiger.app.Styles
import iguanastin.deckconfiger.app.config.hardware.Button
import javafx.event.ActionEvent
import javafx.scene.control.ButtonType
import javafx.scene.control.TextFormatter
import javafx.stage.Window
import javafx.util.Callback
import javafx.util.StringConverter
import tornadofx.*

class ButtonDialog(button: Button? = null, window: Window? = null) : GenericEditDialog<Button>(window) {

    init {
        title = if (button == null) "New Button" else "Edit Button"

        val pinField = textfield {
            promptText = "Pin"
            text = button?.primaryPin?.toString()
            textFormatter = TextFormatter<String> {
                if (it.controlNewText.matches("[0-9]*".toRegex())) {
                    it
                } else {
                    null
                }
            }
        }
        val xField = textfield {
            promptText = "X"
            text = button?.x?.toString() ?: "0"
            textFormatter = TextFormatter<String> {
                if (it.controlNewText.matches("-?[0-9]*".toRegex())) {
                    it
                } else {
                    null
                }
            }
        }
        val yField = textfield {
            promptText = "Y"
            text = button?.y?.toString() ?: "0"
            textFormatter = TextFormatter<String> {
                if (it.controlNewText.matches("-?[0-9]*".toRegex())) {
                    it
                } else {
                    null
                }
            }
        }
        val debounceField = textfield {
            promptText = "Debounce (millis)"
            text = button?.debounceInterval?.toString() ?: "5"
            textFormatter = TextFormatter<String> {
                if (it.controlNewText.matches("[0-9]*".toRegex())) {
                    it
                } else {
                    null
                }
            }
        }
        val detectField = choicebox<Int> {
            items.addAll(0, 1)
            value = button?.detectPress ?: 0
            this.converter = object : StringConverter<Int?>() {
                override fun toString(int: Int?): String {
                    return if (int == 0) "LOW" else "HIGH"
                }

                override fun fromString(string: String?): Int? {
                    return if (string == "LOW") 0 else 1
                }
            }
        }

        vbox.add(hbox(5) {
            label("Pin")
            add(pinField)
        })
        vbox.add(hbox(5) {
            label("Debounce")
            add(debounceField)
        })
        vbox.add(hbox(5) {
            label("Detect")
            add(detectField)
        })
        vbox.add(hbox(5) {
            label("X")
            add(xField)
        })
        vbox.add(hbox(5) {
            label("Y")
            add(yField)
        })

        dialogPane.lookupButton(ButtonType.APPLY).addEventFilter(ActionEvent.ACTION) { event ->
            if (pinField.text.isBlank()) {
                event.consume()
                pinField.addClass(Styles.redBG)
            }
        }

        resultConverter = Callback {
            if (it != ButtonType.APPLY) return@Callback null

            val result = button ?: Button(0, 0, 0)

            result.apply {
                primaryPin = pinField.text.toInt()
                x = xField.text.toIntOrNull() ?: 0
                y = yField.text.toIntOrNull() ?: 0
                debounceInterval = debounceField.text.toIntOrNull() ?: Button.defaultDebounce
                detectPress = detectField.value ?: Button.defaultDetect
            }

            return@Callback result
        }
    }

}