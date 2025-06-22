package iguanastin.deckconfiger.app.config.profile

import org.json.JSONObject
import java.awt.Robot


abstract class ActionCompanion {
    abstract fun fromJSON(j: JSONObject): Action
}

abstract class Action {

    protected abstract val type: String

    companion object : ActionCompanion() {
        private const val JSON_TYPE = "type"

        @JvmStatic
        protected val robot: Robot = Robot()

        @JvmStatic
        protected val types = mutableMapOf<String, ActionCompanion>()

        override fun fromJSON(j: JSONObject): Action {
            return types[j.getString(JSON_TYPE)]!!.fromJSON(j)
        }
    }

    abstract fun perform()

    open fun toJSON(): JSONObject {
        return JSONObject().apply {
            put(JSON_TYPE, type)
        }
    }

}

class KeyAction(val awtkey: Int, val actionType: Type) : Action() {

    override val type: String = KeyAction.type

    enum class Type {
        PRESS,
        RELEASE,
        INSTANT
    }

    companion object : ActionCompanion() {
        private const val JSON_ACTIONTYPE = "actiontype"
        private const val JSON_AWTKEY = "awtkey"
        private const val type: String = "keyaction"

        init {
            types.put(type, this)
        }

        override fun fromJSON(j: JSONObject): Action {
            return KeyAction(j.getInt(JSON_AWTKEY), Type.valueOf(j.getString(JSON_ACTIONTYPE)))
        }
    }

    override fun perform() {
        when (actionType) {
            Type.PRESS -> robot.keyPress(awtkey)
            Type.RELEASE -> robot.keyRelease(awtkey)
            Type.INSTANT -> {
                robot.keyPress(awtkey)
                robot.keyRelease(awtkey)
            }
        }
    }

    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            put(JSON_AWTKEY, awtkey)
            put(JSON_ACTIONTYPE, actionType.toString())
        }
    }

}