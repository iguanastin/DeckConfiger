package iguanastin.deckconfiger.app.config.profile

import org.json.JSONObject

abstract class MultiKeyAction {

    abstract val type: String

    open fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("type", type)
        }
    }

}

class KeyMultiKeyAction(var key: Int, var direction: Direction): MultiKeyAction() {

    constructor(json: JSONObject): this(json.getInt("key"), Direction.valueOf(json.getString("direction")))

    companion object {
        val type = "key_action"
    }

    override val type: String = KeyMultiKeyAction.type

    enum class Direction {
        UP,
        DOWN
    }

    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            put("key", key)
            put("direction", direction)
        }
    }

}

class DelayMultiKeyAction(var time: Long): MultiKeyAction() {

    constructor(json: JSONObject): this(json.getLong("time"))

    companion object {
        val type = "delay_action"
    }

    override val type: String = DelayMultiKeyAction.type

    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            put("time", time)
        }
    }

}