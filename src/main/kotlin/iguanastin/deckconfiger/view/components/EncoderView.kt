package iguanastin.deckconfiger.view.components

import iguanastin.deckconfiger.app.Styles
import iguanastin.deckconfiger.app.config.hardware.Encoder
import javafx.scene.paint.Color
import tornadofx.*

class EncoderView(encoder: Encoder) : HardwareComponentView(encoder) {

    init {
        center = stackpane {
            circle(radius = 25) { fill = Color.DARKGRAY }
            anchorpane {
                label("\uD83E\uDC7F") {
                    addClass(Styles.encoderArrows)
                    anchorpaneConstraints {
                        topAnchor = 2
                        leftAnchor = 2
                    }
                    bindClass(encoder.identProperty.map { if (it && encoder.identLeft) Styles.encoderArrowIdent else null })
                }
                label("\uD83E\uDC7E") {
                    addClass(Styles.encoderArrows)
                    anchorpaneConstraints {
                        topAnchor = 2
                        rightAnchor = 2
                    }
                    bindClass(encoder.identProperty.map { if (it && !encoder.identLeft) Styles.encoderArrowIdent else null })
                }
            }
        }

        // TODO Bind ident left/right
    }

}
