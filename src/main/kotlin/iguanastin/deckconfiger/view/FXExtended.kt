package iguanastin.deckconfiger.view

import iguanastin.deckconfiger.app.MyApp
import javafx.application.Platform
import javafx.event.EventTarget
import tornadofx.*


fun EventTarget.configeditor(app: MyApp, op: ConfigEditor.() -> Unit = {}) = opcr(this, ConfigEditor(app), op)

fun runOnUIThread(op: () -> Unit) {
    if (Platform.isFxApplicationThread()) op()
    else Platform.runLater(op)
}