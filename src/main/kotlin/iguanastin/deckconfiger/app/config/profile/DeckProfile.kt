package iguanastin.deckconfiger.app.config.profile

import javafx.beans.property.SimpleStringProperty
import org.json.JSONObject

// TODO better color/pattern data type?
class DeckProfile(name: String, color: String = "white", pattern: String? = null) {


    val nameProperty = SimpleStringProperty(name)
    var name: String
        get() = nameProperty.get()
        set(value) = nameProperty.set(value)

    val colorProperty = SimpleStringProperty(color)
    var color: String
        get() = colorProperty.get()
        set(value) = colorProperty.set(value)

    val patternProperty = SimpleStringProperty(pattern)
    var pattern: String?
        get() = patternProperty.get()
        set(value) = patternProperty.set(value)


    fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("name", name)
            put("color", color)
            put("pattern", pattern)
        }
    }

    override fun toString(): String {
        return name
    }

    companion object {
        fun fromJSON(json: JSONObject): DeckProfile {
            return DeckProfile(json.getString("name"), json.getString("color"), json.optString("pattern"))
        }
    }

}