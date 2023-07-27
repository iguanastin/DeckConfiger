package iguanastin.deckconfiger.view.components

import iguanastin.deckconfiger.app.config.hardware.Button
import iguanastin.deckconfiger.view.runOnUIThread
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.scene.paint.Color
import javafx.util.Duration
import tornadofx.*

class ButtonView(button: Button) : HardwareComponentView(button) {

    private val identTimeline = Timeline().apply {
        keyFrames.add(KeyFrame(Duration.millis(250.0), {
            runOnUIThread { rect.fill = Color.GRAY }
        }))
        keyFrames.add(KeyFrame(Duration.millis(500.0), {
            runOnUIThread { rect.fill = Color.LIGHTGRAY }
            playFromStart()
        }))
    }

    private val rect = rectangle(width = 50, height = 50) { fill = Color.LIGHTGRAY }

    init {
        top = label("Button")
        center = rect

        button.identProperty.addListener { _, _, new ->
            if (new) identTimeline.playFromStart() else identTimeline.stop()
        }
    }

}
