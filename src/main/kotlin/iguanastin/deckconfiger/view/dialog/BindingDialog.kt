package iguanastin.deckconfiger.view.dialog

import iguanastin.deckconfiger.app.MyApp
import iguanastin.deckconfiger.app.config.hardware.Button
import iguanastin.deckconfiger.app.config.profile.Binding
import iguanastin.deckconfiger.app.config.profile.ButtonBinding
import iguanastin.deckconfiger.app.config.profile.EncoderBinding
import iguanastin.deckconfiger.view.onClick
import javafx.scene.layout.HBox
import tornadofx.*

class BindingDialog(val binding: Binding, val app: MyApp): StackDialog() {

    companion object {
        private val twoActionTypes = arrayOf(ButtonBinding.TYPE, EncoderBinding.TYPE)
    }

    init {
        root.graphic = hbox(50) {
            padding = insets(50)

            if (binding.type in twoActionTypes) init2ActionType()
            else TODO("Unimplemented")
        }

        onClick { if (it.target == this) close() }
    }

    private fun HBox.init2ActionType() {
        val leftText = if (binding is ButtonBinding) "Press" else "Clockwise"
        val rightText = if (binding is ButtonBinding) "Release" else "Counter Clockwise"
        button(leftText) {
            padding = insets(25)
            onClick { app.dialog(EditDialog("Edit Binding", listOf(
                EditStringField("Name", binding.nameProperty),
                EditIntField("ID", binding.idProperty),
                EditChoiceField("Detect", (app.deckConfig!!.hardware.componentByID[binding.id]!! as Button).detectPressProperty,
                    *Button.Detect.values())
            ))) }
        }
        button(rightText) {
            padding = insets(25)
        }
    }

}