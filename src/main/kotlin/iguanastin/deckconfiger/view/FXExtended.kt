package iguanastin.deckconfiger.view

import javafx.application.Platform
import javafx.event.EventTarget
import tornadofx.*


fun EventTarget.configeditor(op: ConfigEditor.() -> Unit = {}) = opcr(this, ConfigEditor(), op)

fun runOnUIThread(op: () -> Unit) {
    if (Platform.isFxApplicationThread()) op()
    else Platform.runLater(op)
}