package iguanastin.deckconfiger.view

import iguanastin.deckconfiger.app.MyApp
import iguanastin.deckconfiger.app.serial.SerialCommunicator
import iguanastin.deckconfiger.view.dialog.TextInputDialog
import iguanastin.deckconfiger.view.dialog.TopEnabledStackPane
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventTarget
import javafx.scene.control.ButtonBase
import javafx.scene.control.MenuItem
import javafx.scene.control.TextField
import tornadofx.*


fun EventTarget.configeditor(app: MyApp, op: ConfigEditor.() -> Unit = {}) = opcr(this, ConfigEditor(app), op)
fun EventTarget.serialconsole(serial: SerialCommunicator, op: SerialConsole.() -> Unit = {}) =
    opcr(this, SerialConsole(serial), op)

fun TopEnabledStackPane.textinputdialog(
    header: String,
    text: String = "",
    prompt: String = "",
    onAccept: (String) -> Unit,
    onClose: () -> Unit = {},
    op: TextInputDialog.() -> Unit = {}
) = opcr(this, TextInputDialog(header, text, prompt, onAccept, onClose), op)

fun EventTarget.topenabledstackpane(op: TopEnabledStackPane.() -> Unit): TopEnabledStackPane =
    TopEnabledStackPane().attachTo(this, op)

fun runOnUIThread(op: () -> Unit) {
    if (Platform.isFxApplicationThread()) op()
    else Platform.runLater(op)
}

fun <T : Event> eventHandlerConsuming(op: (T) -> Unit): EventHandler<T> {
    return EventHandler { event ->
        event.consume()
        op(event)
    }
}

fun ButtonBase.onActionConsuming(op: (ActionEvent) -> Unit) {
    onAction = eventHandlerConsuming(op)
}

fun TextField.onActionConsuming(op: (ActionEvent) -> Unit) {
    onAction = eventHandlerConsuming(op)
}

fun MenuItem.onActionConsuming(op: (ActionEvent) -> Unit) {
    onAction = eventHandlerConsuming(op)
}