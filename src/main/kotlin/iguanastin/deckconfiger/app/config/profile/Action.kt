package iguanastin.deckconfiger.app.config.profile

import org.json.JSONObject

abstract class Action {

    abstract val type: Int

    open fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("type", type)
        }
    }

    companion object {
        const val MOUSE_ACTION = 1
        const val KEYBOARD_ACTION = 2

        fun fromJSON(json: JSONObject): Action {
            val type = json.getInt("type")
            return when (type) {
                MouseAction.type -> MouseAction.fromJSON(json)
                KeyboardAction.type -> KeyboardAction.fromJSON(json)
                else -> throw IllegalArgumentException("Invalid action type: $type")
            }
        }
    }

}