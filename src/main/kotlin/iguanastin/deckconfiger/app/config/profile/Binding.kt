package iguanastin.deckconfiger.app.config.profile

import javafx.beans.property.SimpleIntegerProperty
import org.json.JSONObject

class Binding(hwID: Int) {

    val hwIDProperty = SimpleIntegerProperty(hwID)
    var hwID: Int
        get() = hwIDProperty.get()
        set(value) = hwIDProperty.set(value)


    fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("id", hwID)
            // TODO
        }
    }


    companion object {
        fun fromJSON(json: JSONObject): Binding {
            return Binding(json.getInt("id"))
        }
    }

}