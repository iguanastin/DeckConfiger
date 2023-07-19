package iguanastin.deckconfiger.app

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val noConfigWarning by cssclass()

        val baseColor = c("#3b3f42")
    }

    init {
        root {
            baseColor = Styles.baseColor
            backgroundColor += Styles.baseColor
        }

        label and heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }

        noConfigWarning {
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            textFill = Color.LIGHTGRAY
        }
    }
}