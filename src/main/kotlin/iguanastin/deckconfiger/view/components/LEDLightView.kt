package iguanastin.deckconfiger.view.components

import iguanastin.deckconfiger.app.config.hardware.LEDLight
import javafx.scene.paint.Color
import tornadofx.*

class LEDLightView(led: LEDLight): HardwareComponentView(led) {

    init {
        top = label("LED Light")
        center = circle(radius = 15) { fill = Color.LIGHTGREEN }
    }

}