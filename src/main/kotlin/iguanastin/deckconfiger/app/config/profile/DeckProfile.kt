package iguanastin.deckconfiger.app.config.profile

import org.json.JSONObject

// TODO better color/pattern data type?
class DeckProfile(var name: String, var color: String, var pattern: String? = null) {

    fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("name", name)
            put("color", color)
            put("pattern", pattern)
        }
    }

    companion object {
        fun fromJSON(json: JSONObject): DeckProfile {
            return DeckProfile(json.getString("name"), json.getString("color"), if (json.has("pattern")) json.getString("pattern") else null)
        }
    }

}