package iguanastin.deckconfiger.app.config.profile

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import org.json.JSONObject
import tornadofx.getValue
import tornadofx.setValue

class MouseAction: Action() {

    enum class Button(val buttonId: Int) {
        LEFT(1),
        RIGHT(2),
        MIDDLE(4),
        BACK(8),
        FORWARD(16)
    }

    override val type: Int = MouseAction.type

    val buttonProperty = SimpleObjectProperty<Button>()
    var button by buttonProperty

    val pressProperty = SimpleBooleanProperty()
    var press by pressProperty

    val releaseProperty = SimpleBooleanProperty()
    var release by releaseProperty

    val scrollXProperty = SimpleIntegerProperty()
    var scrollX by scrollXProperty

    val scrollYProperty = SimpleIntegerProperty()
    var scrollY by scrollYProperty

    val moveXProperty = SimpleIntegerProperty()
    var moveX by moveXProperty

    val moveYProperty = SimpleIntegerProperty()
    var moveY by moveYProperty


    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            if (button != null) putOpt(JSON_BUTTON, button.buttonId)
            if (press) putOpt(JSON_PRESS, press)
            if (release) putOpt(JSON_RELEASE, release)
            if (moveX != 0) putOpt(JSON_MOVE_X, moveX)
            if (moveY != 0) putOpt(JSON_MOVE_Y, moveY)
            if (scrollX != 0) putOpt(JSON_SCROLL_X, scrollX)
            if (scrollY != 0) putOpt(JSON_SCROLL_Y, scrollY)
        }
    }

    companion object {
        const val type = MOUSE_ACTION

        const val JSON_BUTTON = "button"
        const val JSON_PRESS = "press"
        const val JSON_RELEASE = "release"
        const val JSON_SCROLL_X = "scrollx"
        const val JSON_SCROLL_Y = "scrolly"
        const val JSON_MOVE_X = "movex"
        const val JSON_MOVE_Y = "movey"

        fun fromJSON(json: JSONObject): MouseAction {
            return MouseAction().apply {
                button = Button.values().singleOrNull { it.buttonId == json.optInt(JSON_BUTTON) }
                press = json.optBoolean(JSON_PRESS)
                release = json.optBoolean(JSON_RELEASE)
                scrollX = json.optInt(JSON_SCROLL_X)
                scrollY = json.optInt(JSON_SCROLL_Y)
                moveX = json.optInt(JSON_MOVE_X)
                moveY = json.optInt(JSON_MOVE_Y)
            }
        }
    }

}