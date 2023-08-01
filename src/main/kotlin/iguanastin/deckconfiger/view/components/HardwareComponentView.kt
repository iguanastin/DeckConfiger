package iguanastin.deckconfiger.view.components

import iguanastin.deckconfiger.app.Styles
import iguanastin.deckconfiger.app.config.hardware.HardwareComponent
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.layout.BorderPane
import tornadofx.*

abstract class HardwareComponentView(component: HardwareComponent) : BorderPane() {

    val componentProperty = SimpleObjectProperty(component)
    var component by componentProperty

    val draggableProperty = SimpleBooleanProperty(false)
    var draggable by draggableProperty

    private var dragOffsetX: Double = 0.0
    private var dragOffsetY: Double = 0.0
    private var dragging: Boolean = false

    init {
        addClass(Styles.component)
        draggableProperty.addListener { _, _, new ->
            if (new) addClass(Styles.draggable)
            else removeClass(Styles.draggable)
        }

        top = label {
            textProperty().bind(component.nameProperty)
        }

        isPickOnBounds = false

        layoutXProperty().bind(component.xProperty)
        layoutYProperty().bind(component.yProperty)

        bindClass(component.identProperty.map { if (it) Styles.ident else null })

        initDragBehavior()
    }

    private fun initDragBehavior() {
        // Init drag listeners
        onDragDetected = EventHandler { event ->
            if (draggable && event.button == MouseButton.PRIMARY) {
                event.consume()
                dragOffsetX = event.x
                dragOffsetY = event.y
                dragging = true
                startFullDrag()
            }
        }
        onMouseDragged = EventHandler { event ->
            if (dragging) {
                event.consume()
                component.x += (event.x - dragOffsetX).toInt()
                component.y += (event.y - dragOffsetY).toInt()
            }
        }
        onMouseReleased = EventHandler { event ->
            event.consume()
            dragging = false
        }
    }

}