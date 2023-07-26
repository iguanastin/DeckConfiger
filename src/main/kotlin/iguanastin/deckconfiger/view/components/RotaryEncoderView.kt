package iguanastin.deckconfiger.view.components

import iguanastin.deckconfiger.app.config.hardware.Encoder
import javafx.scene.paint.Color
import tornadofx.*

class RotaryEncoderView(encoder: Encoder) : HardwareComponentView(encoder) {

    init {
        top = label("Encoder Dial")
        center = circle(radius = 25) { fill = Color.DARKGRAY }
    }

}
