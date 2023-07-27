package iguanastin.deckconfiger.view.dialog

import iguanastin.deckconfiger.app.Styles
import iguanastin.deckconfiger.app.config.hardware.Encoder
import javafx.event.ActionEvent
import javafx.scene.control.ButtonType
import javafx.scene.control.TextFormatter
import javafx.stage.Window
import javafx.util.Callback
import tornadofx.*

class EncoderDialog(encoder: Encoder? = null, window: Window? = null) : GenericEditDialog<Encoder>(window) {

    init {
        title = if (encoder == null) "New Encoder" else "Edit Encoder"

        val pin1Field = textfield {
            promptText = "Pin1"
            text = encoder?.primaryPin?.toString()
            textFormatter = TextFormatter<String> {
                if (it.controlNewText.matches("[0-9]*".toRegex())) {
                    it
                } else {
                    null
                }
            }
        }
        val pin2Field = textfield {
            promptText = "Pin2"
            text = encoder?.secondaryPin?.toString()
            textFormatter = TextFormatter<String> {
                if (it.controlNewText.matches("[0-9]*".toRegex())) {
                    it
                } else {
                    null
                }
            }
        }

        vbox.add(hbox(5) {
            label("Pin1")
            add(pin1Field)
        })
        vbox.add(hbox(5) {
            label("Pin2")
            add(pin2Field)
        })

        dialogPane.lookupButton(ButtonType.APPLY).addEventFilter(ActionEvent.ACTION) { event ->
            if (pin1Field.text.isBlank()) {
                event.consume()
                pin1Field.addClass(Styles.redBG)
            }
            if (pin2Field.text.isBlank()) {
                event.consume()
                pin2Field.addClass(Styles.redBG)
            }
        }

        resultConverter = Callback {
            if (it != ButtonType.APPLY) return@Callback null

            val result = encoder ?: Encoder(0, 0, 0, 0)

            result.apply {
                primaryPin = pin1Field.text.toInt()
                secondaryPin = pin2Field.text.toInt()
            }

            return@Callback result
        }
    }

}