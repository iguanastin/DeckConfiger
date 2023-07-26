package iguanastin.deckconfiger.view.dialog

import iguanastin.deckconfiger.app.Styles
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Window
import tornadofx.*

abstract class GenericEditDialog<R>(window: Window? = null): Dialog<R>() {

    protected val vbox: VBox

    init {
        initModality(Modality.WINDOW_MODAL)
        initOwner(window)

        dialogPane.apply {
            vbox = vbox(10)
            graphic = vbox
            buttonTypes.addAll(ButtonType.APPLY, ButtonType.CANCEL)
        }
    }

}