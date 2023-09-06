package iguanastin.deckconfiger.view.components

import iguanastin.deckconfiger.app.config.hardware.RGBLight
import iguanastin.deckconfiger.view.runOnUIThread
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.scene.paint.Color
import javafx.util.Duration
import tornadofx.*

class RGBLightView(led: RGBLight): HardwareComponentView(led) {

    private val identTimeline = Timeline().apply {
        val colors = arrayOf(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.INDIGO, Color.VIOLET, Color.BLACK, Color.WHITE)

        val n = 9
        for (i in 0..n) {
            keyFrames.add(KeyFrame(Duration.millis(i * 250.0), {
                runOnUIThread { circle.fill = colors[i] }
            }))
        }
        keyFrames.add(KeyFrame(Duration.millis(n * 250.0), {
            runOnUIThread { circle.fill = Color.RED }
        }))
    }

    private val circle = circle(radius = 15) { fill = Color.RED }

    init {
        center = circle
    }

    fun ident() {
        identTimeline.playFromStart()
    }

}