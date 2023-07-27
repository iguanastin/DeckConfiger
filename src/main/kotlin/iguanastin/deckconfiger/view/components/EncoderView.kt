package iguanastin.deckconfiger.view.components

import iguanastin.deckconfiger.app.config.hardware.Encoder
import iguanastin.deckconfiger.view.runOnUIThread
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.scene.paint.Color
import javafx.util.Duration
import tornadofx.*

class EncoderView(encoder: Encoder) : HardwareComponentView(encoder) {

    private val circle = circle(radius = 25) { fill = Color.DARKGRAY }

    init {
        top = label("Encoder Dial")
        center = circle

        // TODO Bind ident left/right
    }

}
