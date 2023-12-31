package iguanastin.deckconfiger.view.dialog

import javafx.beans.InvalidationListener
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Node
import javafx.scene.layout.StackPane
import tornadofx.*

class TopEnabledStackPane : StackPane() {

    private val current: ObjectProperty<Node?> = SimpleObjectProperty(null)

    init {
        children.addListener(InvalidationListener {
            if (children.isNotEmpty()) {
                val last = children.last()
                children.forEach {
                    it.isDisable = it !== last
                }

                if (last !== current.get()) {
                    current.set(last)
                }
            }
        })

        current.addListener(ChangeListener { _, _, newValue ->
            newValue?.requestFocus()
        })
    }

}