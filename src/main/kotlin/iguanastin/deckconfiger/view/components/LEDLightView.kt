package iguanastin.deckconfiger.view.components

import iguanastin.deckconfiger.app.config.hardware.LEDLight
import iguanastin.deckconfiger.view.runOnUIThread
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.scene.paint.Color
import javafx.util.Duration
import tornadofx.*

class LEDLightView(led: LEDLight): HardwareComponentView(led) {

    private val identTimeline = Timeline().apply {
        for (i in 0..5) {
            keyFrames.add(KeyFrame(Duration.millis(i * 500.0), {
                runOnUIThread { circle.fill = Color.GREEN }
            }))
            keyFrames.add(KeyFrame(Duration.millis(i * 500.0 + 250.0), {
                runOnUIThread { circle.fill = Color.LIGHTGREEN }
            }))
        }
    }

    private val circle = circle(radius = 15) { fill = Color.LIGHTGREEN }

    init {
        top = label("LED Light")
        center = circle
    }

    override fun ident() {
        identTimeline.playFromStart()
    }

}