package iguanastin.deckconfiger.app.config.profile

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import org.json.JSONObject
import tornadofx.*

class Binding(hwID: Int) {

    val hwIDProperty = SimpleIntegerProperty(hwID)
    var hwID by hwIDProperty

    val action1Property = SimpleObjectProperty<Action?>()
    var action1 by action1Property

    val action2Property = SimpleObjectProperty<Action?>()
    var action2 by action2Property


    fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("id", hwID)
            putOpt("action1", action1?.toJSON())
            putOpt("action2", action2?.toJSON())
        }
    }


    companion object {
        fun fromJSON(json: JSONObject): Binding {
            return Binding(json.getInt("id")).apply {
                json.optJSONObject("action1")?.also { action1 = Action.fromJSON(it) }
                json.optJSONObject("action2")?.also { action2 = Action.fromJSON(it) }
            }
        }
    }

}