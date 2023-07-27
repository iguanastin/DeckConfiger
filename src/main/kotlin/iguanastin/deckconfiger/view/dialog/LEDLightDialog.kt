package iguanastin.deckconfiger.view.dialog

import iguanastin.deckconfiger.app.Styles
import iguanastin.deckconfiger.app.config.hardware.HardwareDefinition
import iguanastin.deckconfiger.app.config.hardware.LEDLight
import javafx.event.ActionEvent
import javafx.scene.control.ButtonType
import javafx.scene.control.TextFormatter
import javafx.stage.Window
import javafx.util.Callback
import tornadofx.*

class LEDLightDialog(hardware: HardwareDefinition, led: LEDLight? = null, window: Window? = null) : GenericEditDialog<LEDLight>(window) {

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

        vbox.add(hbox(5) {
            label("Pin")
            add(pinField)
        })

        dialogPane.lookupButton(ButtonType.APPLY).addEventFilter(ActionEvent.ACTION) { event ->
            if (pinField.text.isBlank()) {
                event.consume()
                pinField.addClass(Styles.redBG)
            }
        }

        resultConverter = Callback {
            if (it != ButtonType.APPLY) return@Callback null

            val result = led ?: LEDLight(hardware.getNextID(), 0, 0, 0)
            result.primaryPin = pinField.text.toInt()

            return@Callback result
        }
    }

}