package iguanastin.deckconfiger.app.config.profile

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import org.json.JSONObject
import tornadofx.getValue
import tornadofx.setValue

class MouseAction: Action() {

    override val type: Int = MouseAction.type

    val buttonProperty = SimpleIntegerProperty()
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
            putOpt("button", button)
            putOpt("press", press)
            putOpt("release", release)
            putOpt("movey", moveX)
            putOpt("movey", moveY)
            putOpt("scrollx", scrollX)
            putOpt("scrolly", scrollY)
        }
    }

    companion object {
        const val type = MOUSE_ACTION

        fun fromJSON(json: JSONObject): MouseAction {
            return MouseAction().apply {
                button = json.optInt("button")
                press = json.optBoolean("press")
                release = json.optBoolean("release")
                scrollX = json.optInt("scrollx")
                scrollY = json.optInt("scrolly")
                moveX = json.optInt("movex")
                moveY = json.optInt("movey")
            }
        }
    }

}