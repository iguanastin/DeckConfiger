package iguanastin.deckconfiger.app.config.profile

import org.json.JSONObject

class SingleKeyPress(var key: Int, var instantaneous: Boolean = false): Action() {

    constructor(json: JSONObject): this(json.getInt("key"), json.getBoolean("instant"))

    companion object {
        val type: String = "single_key_press"
    }

    override val type: String = SingleKeyPress.type

    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            put("key", key)
            put("instant", instantaneous)
        }
    }

}