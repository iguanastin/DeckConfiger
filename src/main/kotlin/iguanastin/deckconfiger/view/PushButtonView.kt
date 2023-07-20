package iguanastin.deckconfiger.view

import iguanastin.deckconfiger.app.config.hardware.PushButton
import javafx.scene.paint.Color
import tornadofx.*

class PushButtonView(button: PushButton) : HardwareComponentView(button) {

    init {
        top = label("Button")
        center = rectangle(width = 50, height = 50) { fill = Color.LIGHTGRAY }
    }

}
