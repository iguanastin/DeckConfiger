package iguanastin.deckconfiger.app.config.profile

import org.json.JSONObject

abstract class Action {

    abstract val type: String

    open fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("type", type)
        }
    }

}