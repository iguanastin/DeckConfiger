package iguanastin.deckconfiger.app

import javafx.scene.Cursor
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val component by cssclass()
        val draggable by cssclass()

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

        component and hover {
            scaleX = 1.05
            scaleY = 1.05
            cursor = Cursor.HAND
        }
        component and draggable and hover {
            cursor = Cursor.MOVE
        }
    }
}