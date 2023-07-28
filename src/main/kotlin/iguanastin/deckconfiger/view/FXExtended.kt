package iguanastin.deckconfiger.view

import iguanastin.deckconfiger.app.MyApp
import iguanastin.deckconfiger.app.serial.SerialCommunicator
import javafx.application.Platform
import javafx.event.EventTarget
import tornadofx.*


fun EventTarget.configeditor(app: MyApp, op: ConfigEditor.() -> Unit = {}) = opcr(this, ConfigEditor(app), op)
fun EventTarget.serialconsole(serial: SerialCommunicator, op: SerialConsole.() -> Unit = {}) = opcr(this, SerialConsole(serial), op)

fun runOnUIThread(op: () -> Unit) {
    if (Platform.isFxApplicationThread()) op()
    else Platform.runLater(op)
}