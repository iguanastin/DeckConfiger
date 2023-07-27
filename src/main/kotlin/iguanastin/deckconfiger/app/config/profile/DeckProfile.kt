package iguanastin.deckconfiger.app.config.profile

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import org.json.JSONObject

class DeckProfile(name: String, r: Int = 255, g: Int = 255, b: Int = 255) {


    val nameProperty = SimpleStringProperty(name)
    var name: String
        get() = nameProperty.get()
        set(value) = nameProperty.set(value)

    val rProperty = SimpleIntegerProperty(r)
    var r: Int
        get() = rProperty.get()
        set(value) = rProperty.set(value)

    val gProperty = SimpleIntegerProperty(g)
    var g: Int
        get() = gProperty.get()
        set(value) = gProperty.set(value)

    val bProperty = SimpleIntegerProperty(b)
    var b: Int
        get() = bProperty.get()
        set(value) = bProperty.set(value)


    fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("name", name)
            put("r", r)
            put("g", g)
            put("b", b)
        }
    }

    override fun toString(): String {
        return name
    }

    companion object {
        fun fromJSON(json: JSONObject): DeckProfile {
            return DeckProfile(json.getString("name"), json.optInt("r"), json.optInt("g"), json.optInt("b"))
        }
    }

}