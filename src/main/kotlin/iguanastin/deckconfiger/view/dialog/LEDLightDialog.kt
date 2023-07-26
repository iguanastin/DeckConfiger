package iguanastin.deckconfiger.view.dialog

import iguanastin.deckconfiger.app.Styles
import iguanastin.deckconfiger.app.config.hardware.LEDLight
import javafx.event.ActionEvent
import javafx.scene.control.ButtonType
import javafx.scene.control.TextFormatter
import javafx.stage.Window
import javafx.util.Callback
import tornadofx.*

class LEDLightDialog(led: LEDLight? = null, window: Window? = null) : GenericEditDialog<LEDLight>(window) {

    init {
        title = if (led == null) "New LED Light" else "Edit LED Light"

        val pinField = textfield {
            promptText = "Pin"
            if (led != null) text = led.primaryPin.toString()
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
            if (led != null) text = led.x.toString()
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
            if (led != null) text = led.y.toString()
            textFormatter = TextFormatter<String> {
                if (it.controlNewText.matches("-?[0-9]*".toRegex())) {
                    it
                } else {
                    null
                }
            }
        }

        vbox.add(hbox(5) {
            label("Pin")
            add(pinField)
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

            val result = led ?: LEDLight(0, 0, 0)
            result.primaryPin = pinField.text.toInt()
            result.x = xField.text.toIntOrNull() ?: 0
            result.y = yField.text.toIntOrNull() ?: 0

            return@Callback result
        }
    }

}