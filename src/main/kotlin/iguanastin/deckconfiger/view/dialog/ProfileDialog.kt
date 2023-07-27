package iguanastin.deckconfiger.view.dialog

import iguanastin.deckconfiger.app.Styles
import iguanastin.deckconfiger.app.config.profile.DeckProfile
import javafx.event.ActionEvent
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.paint.Color
import javafx.stage.Modality
import javafx.stage.Window
import javafx.util.Callback
import tornadofx.*

class ProfileDialog(val profile: DeckProfile? = null, window: Window? = null) : Dialog<DeckProfile>() {

    init {
        initModality(Modality.WINDOW_MODAL)
        initOwner(window)
        title = if (profile == null) "New Profile" else "Edit Profile"

        val nameField = textfield {
            promptText = "Profile name"
            if (profile != null) text = profile.name
        }
        val colorField = colorpicker(
            if (profile == null) Color.WHITE else Color.color(
                profile.r / 255.0,
                profile.g / 255.0,
                profile.b / 255.0
            )
        )

        dialogPane.apply {
            graphic = vbox(10) {
                hbox(5) {
                    label("Name")
                    add(nameField)
                }
                hbox(5) {
                    label("Color")
                    add(colorField)
                }
            }
            buttonTypes.addAll(ButtonType.APPLY, ButtonType.CANCEL)
            lookupButton(ButtonType.APPLY).addEventFilter(ActionEvent.ACTION) { event ->
                if (nameField.text.isBlank()) {
                    event.consume()
                    nameField.addClass(Styles.redBG)
                }
            }
        }

        resultConverter = Callback {
            if (it != ButtonType.APPLY) return@Callback null

            var result = profile
            if (result == null) result = DeckProfile("temp", 0, 0, 0)

            val c = colorField.value
            result.name = nameField.text
            result.r = (c.red * 255).toInt()
            result.g = (c.green * 255).toInt()
            result.b = (c.blue * 255).toInt()

            return@Callback result
        }
    }

}