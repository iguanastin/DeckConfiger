package iguanastin.deckconfiger.view.components

import iguanastin.deckconfiger.app.config.hardware.Button
import javafx.scene.paint.Color
import tornadofx.*

class ButtonView(button: Button) : HardwareComponentView(button) {

    init {
        top = label("Button")
        center = rectangle(width = 50, height = 50) { fill = Color.LIGHTGRAY }
    }

}
