package iguanastin.deckconfiger.app

import javafx.scene.Cursor
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val component by cssclass()
        val draggable by cssclass()
        val redBG by cssclass()
        val ident by cssclass()
        val textRed by cssclass()
        val textGreen by cssclass()
        val connectedIcon by cssclass()
        val dialogPane by cssclass()
        val dialogRoot by cssclass()
        val dialogHeader by cssclass()

        val baseColor = c("#3b3f42")
    }

    init {
        root {
            baseColor = Styles.baseColor
        }

        label and heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }

        component and hover {
            scaleX = 1.05
            scaleY = 1.05
            cursor = Cursor.HAND
        }
        component and draggable and hover {
            cursor = Cursor.MOVE
        }

        dialogPane {
            backgroundColor += Styles.baseColor
            effect = DropShadow(50.0, c("black")).apply { spread = 0.25 }
        }

        dialogRoot {
            padding = box(20.px)
        }

        dialogHeader {
            fontSize = 1.5.em
            fontWeight = FontWeight.BOLD
        }

        ident {
            backgroundColor += Color.RED
            effect = DropShadow(5.0, Color.YELLOW).apply { spread = 5.0 }
        }

        redBG {
            backgroundColor += Color.RED
        }

        connectedIcon {
            backgroundColor += Styles.baseColor
            backgroundRadius += box(5.px)
            padding = box(5.px)
            fontWeight = FontWeight.EXTRA_BOLD
        }

        textRed {
            textFill = Color.RED.desaturate().brighter()
        }
        textGreen {
            textFill = Color.GREEN
        }
    }
}