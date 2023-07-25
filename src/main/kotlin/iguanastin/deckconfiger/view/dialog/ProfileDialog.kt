package iguanastin.deckconfiger.view.dialog

import iguanastin.deckconfiger.app.Styles
import iguanastin.deckconfiger.app.config.profile.DeckProfile
import javafx.event.ActionEvent
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.stage.Modality
import javafx.stage.Window
import javafx.util.Callback
import tornadofx.*

class ProfileDialog(val profile: DeckProfile? = null, window: Window? = null): Dialog<DeckProfile>() {

    init {
        initModality(Modality.WINDOW_MODAL)
        initOwner(window)
        title = if (profile == null) "New Profile" else "Edit Profile"

        val nameField = textfield {
            promptText = "Profile name"
            if (profile != null) text = profile.name
        }
        val colorField = textfield {
            promptText = "Profile Color"
            if (profile != null) text = profile.color
        }
        val patternField = textfield {
            promptText = "N/A"
            if (profile != null) text = profile.pattern
        }

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
                hbox(5) {
                    label("Pattern")
                    add(patternField)
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
            if (result == null) {
                result = DeckProfile(nameField.text, colorField.text, patternField.text)
            } else {
                result.name = nameField.text
                result.color = colorField.text
                result.pattern = patternField.text
                if (result.pattern?.isBlank() == true) result.pattern = null
            }

            return@Callback result
        }
    }

}