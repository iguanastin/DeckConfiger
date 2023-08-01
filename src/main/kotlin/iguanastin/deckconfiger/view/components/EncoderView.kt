package iguanastin.deckconfiger.view.components

import iguanastin.deckconfiger.app.config.hardware.Encoder
import javafx.scene.paint.Color
import tornadofx.*

class EncoderView(encoder: Encoder) : HardwareComponentView(encoder) {

    private val circle = circle(radius = 25) { fill = Color.DARKGRAY }

    init {
        center = circle

        // TODO Bind ident left/right
    }

}
