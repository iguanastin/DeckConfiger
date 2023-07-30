package iguanastin.deckconfiger.view.dialog

import iguanastin.deckconfiger.app.Styles
import iguanastin.deckconfiger.app.config.profile.DeckProfile
import iguanastin.deckconfiger.view.onActionConsuming
import javafx.geometry.Pos
import javafx.scene.control.ColorPicker
import javafx.scene.control.TextField
import javafx.scene.paint.Color
import tornadofx.*

class EditProfileDialog(
    profile: DeckProfile? = null,
    onAccept: (DeckProfile) -> Unit = {},
    onClose: () -> Unit = {}
) : StackDialog(onClose) {

    private lateinit var nameField: TextField
    private lateinit var colorField: ColorPicker
    private lateinit var acceptButton: javafx.scene.control.Button
    private lateinit var cancelButton: javafx.scene.control.Button

    init {
        root.graphic = vbox(10) {
            addClass(Styles.dialogRoot)
            label(if (profile == null) "New profile" else "Edit profile") { addClass(Styles.dialogHeader) }
            hbox(5) {
                alignment = Pos.CENTER_LEFT
                label("Name:")
                nameField = textfield(profile?.name) {
                    promptText = "Name"
                }
            }
            hbox(5) {
                alignment = Pos.CENTER_LEFT
                label("Color:")
                colorField = colorpicker(
                    if (profile == null) Color.WHITE else Color.color(
                        profile.r / 255.0,
                        profile.g / 255.0,
                        profile.b / 255.0
                    )
                )
            }
            hbox {
                alignment = Pos.CENTER_RIGHT
                acceptButton = button("Ok") {
                    onActionConsuming {
                        if (nameField.text.isNullOrBlank()) {
                            nameField.addClass(Styles.redBG)
                            return@onActionConsuming
                        }
                        close()

                        var result = profile
                        if (result == null) result = DeckProfile("", 0, 0, 0)
                        val c = colorField.value
                        result.name = nameField.text
                        result.r = (c.red * 255).toInt()
                        result.g = (c.green * 255).toInt()
                        result.b = (c.blue * 255).toInt()

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
        nameField.requestFocus()
    }

}