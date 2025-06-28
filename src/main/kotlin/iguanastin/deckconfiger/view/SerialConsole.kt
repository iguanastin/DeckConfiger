package iguanastin.deckconfiger.view

import iguanastin.deckconfiger.app.serial.SerialCommunicator
import javafx.scene.control.TextArea
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import tornadofx.*

class SerialConsole(private val serial: SerialCommunicator) : VBox(5.0) {

    private lateinit var outputArea: TextArea

    init {
        padding = insets(5)
        textfield {
            promptText = "Type a message and press enter to send on serial bus"
            onActionConsuming {
                runOnUIThread {
                    serial.sendRaw((text + "\n").toByteArray(Charsets.US_ASCII))
                    text = null
                }
            }
        }
        separator()
        scrollpane(fitToWidth = true, fitToHeight = true) {
            vgrow = Priority.ALWAYS
            outputArea = textarea {
                isWrapText = true
                isEditable = false
                promptText = "No console output yet"
            }
        }

        serial.rawSerialHandler = {
            val char = it.toInt().toChar()
            print(char)
            runOnUIThread { outputArea.text += char }
        }
    }

}