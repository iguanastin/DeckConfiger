package iguanastin.deckconfiger.view.dialog

import iguanastin.deckconfiger.app.Styles
import iguanastin.deckconfiger.view.onActionConsuming
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import tornadofx.*

class TextInputDialog(header: String, text: String = "", prompt: String = "", onAccept: (String) -> Unit, onClose: () -> Unit = {}): StackDialog(onClose) {

    private val textField: TextField
    private lateinit var acceptButton: Button
    private lateinit var cancelButton: Button

    init {
        root.graphic = VBox(10.0).apply {
            addClass(Styles.dialogRoot)
            minWidth = 300.0

            label(header) { addClass(Styles.dialogHeader) }
            textField = textfield(text) {
                promptText = prompt
                onActionConsuming { acceptButton.fire() }
            }
            hbox {
                alignment = Pos.CENTER_RIGHT
                acceptButton = button("Ok") {
                    onActionConsuming {
                        close()
                        onAccept(textField.text)
                    }
                }
                cancelButton = button("Cancel") {
                    onActionConsuming { close() }
                }
            }
        }
    }

    override fun requestFocus() {
        textField.requestFocus()
    }

}