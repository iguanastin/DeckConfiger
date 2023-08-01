package iguanastin.deckconfiger.app.config.hardware

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import org.json.JSONObject
import tornadofx.getValue
import tornadofx.setValue

abstract class HardwareComponent(json: JSONObject? = null, id: Int = -1) {

    val idProperty = SimpleIntegerProperty()
    var id by idProperty

    val nameProperty = SimpleStringProperty("")
    var name: String by nameProperty

    val primaryPinProperty = SimpleIntegerProperty()
    var primaryPin by primaryPinProperty

    val xProperty = SimpleIntegerProperty()
    var x by xProperty

    val yProperty = SimpleIntegerProperty()
    var y by yProperty

    val identProperty = SimpleBooleanProperty(false)
    var ident by identProperty

    abstract val type: String


    init {
        this.id = json?.optInt("id") ?: id
        name = json?.optString("name") ?: ""
        primaryPin = json?.optInt("pin") ?: 0
        x = json?.optInt("x") ?: 0
        y = json?.optInt("y") ?: 0
    }


    open fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("name", name)
            put("type", type)
            put("pin", primaryPin)
            put("x", x)
            put("y", y)
        }
    }

}