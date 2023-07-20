package iguanastin.deckconfiger.view

import javafx.event.EventTarget
import tornadofx.*


fun EventTarget.configeditor(op: ConfigEditor.() -> Unit = {}) = opcr(this, ConfigEditor(), op)