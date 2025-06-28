package iguanastin.deckconfiger.app.config.profile

import org.json.JSONObject
import tornadofx.*
import java.awt.MouseInfo
import java.awt.Robot
import java.awt.event.InputEvent


abstract class ActionCompanion {
    abstract operator fun invoke(j: JSONObject): Action
}

abstract class Action {

    protected abstract val type: String

    companion object : ActionCompanion() {
        private const val JSON_TYPE = "type"

        @JvmStatic
        protected val robot: Robot = Robot()

        @JvmStatic
        protected val types = mapOf<String, ActionCompanion>(
            WaitAction.TYPE.to(WaitAction),
            ActionSequence.TYPE.to(ActionSequence),
            MouseButtonAction.TYPE.to(MouseButtonAction),
            MouseMoveAction.TYPE.to(MouseMoveAction),
            MouseWheelAction.TYPE.to(MouseWheelAction),
            KeyboardAction.TYPE.to(KeyboardAction),
        )

        override fun invoke(j: JSONObject): Action {
            return types[j.getString(JSON_TYPE)]!!(j)
        }
    }

    abstract fun perform()

    open fun toJSON(): JSONObject {
        return JSONObject().apply {
            put(JSON_TYPE, type)
        }
    }

}

class ActionSequence() : Action() {

    override val type: String = TYPE

    val actions = mutableListOf<Action>()

    companion object : ActionCompanion() {
        private const val JSON_ACTIONS = "actions"
        const val TYPE: String = "sequenceaction"

        override fun invoke(j: JSONObject): Action {
            return ActionSequence().apply {
                actions.addAll(j.getJSONArray(JSON_ACTIONS).map { Action(it as JSONObject) })
            }
        }
    }

    override fun perform() {
        actions.forEach { it.perform() }
    }

    override fun toJSON(): JSONObject {
        return super.toJSON().apply { put(JSON_ACTIONS, actions.map { it.toJSON() }) }
    }

}

class WaitAction(millis: Long = 1000) : Action() {

    override val type: String = TYPE

    val millisProperty = longProperty(millis)
    var millis by millisProperty

    companion object : ActionCompanion() {
        private const val JSON_MILLIS = "millis"
        const val TYPE = "waitaction"

        override fun invoke(j: JSONObject): Action {
            return WaitAction(j.getLong(JSON_MILLIS))
        }
    }

    override fun perform() {
        Thread.sleep(millis)
    }

    override fun toJSON(): JSONObject {
        return super.toJSON().apply { put(JSON_MILLIS, millis) }
    }

}

abstract class ButtonAction(button: Int, actionType: Type, json: JSONObject? = null) : Action() {

    enum class Type(val mask: Int) {
        PRESS(1 shl 0),
        RELEASE(1 shl 1),
        INSTANT(PRESS.mask or RELEASE.mask),
    }

    val buttonProperty = intProperty(button)
    var button by buttonProperty

    val actionTypeProperty = objectProperty(actionType)
    var actionType by actionTypeProperty

    init {
        json?.also { json ->
            this.button = json.getInt(JSON_BUTTON)
            this.actionType = Type.valueOf(json.getString(JSON_ACTION_TYPE))
        }
    }

    companion object {
        private const val JSON_BUTTON = "button"
        private const val JSON_ACTION_TYPE = "actiontype"
    }

    abstract fun pressPerformer()
    abstract fun releasePerformer()

    override fun perform() {
        if (actionType.mask and Type.PRESS.mask != 0) pressPerformer()
        if (actionType.mask and Type.RELEASE.mask != 0) releasePerformer()
    }

    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            put(JSON_BUTTON, button)
            put(JSON_ACTION_TYPE, actionType.toString())
        }
    }

}

class MouseButtonAction(
    button: Int = InputEvent.getMaskForButton(1),
    actionType: Type = Type.INSTANT,
    json: JSONObject? = null
) : ButtonAction(button, actionType, json) {

    override val type: String = TYPE

    companion object : ActionCompanion() {
        const val TYPE = "mousebutton"

        override fun invoke(j: JSONObject): Action {
            return MouseButtonAction(json = j)
        }
    }

    override fun pressPerformer() = robot.mousePress(button)

    override fun releasePerformer() = robot.mouseRelease(button)

}

class KeyboardAction(awtKey: Int = 0, actionType: Type = Type.INSTANT, json: JSONObject? = null) :
    ButtonAction(awtKey, actionType, json) {

    override val type: String = TYPE

    companion object : ActionCompanion() {
        const val TYPE = "keyaction"

        override fun invoke(j: JSONObject): Action {
            return KeyboardAction(json = j)
        }
    }

    override fun pressPerformer() = robot.keyPress(button)

    override fun releasePerformer() = robot.keyRelease(button)

}

class MouseMoveAction(x: Int, y: Int, delta: Boolean) : Action() {

    override val type: String = TYPE

    val xProperty = intProperty(x)
    var x by xProperty

    val yProperty = intProperty(y)
    var y by yProperty

    val deltaProperty = booleanProperty(delta)
    var delta by deltaProperty

    companion object : ActionCompanion() {
        private const val JSON_X = "x"
        private const val JSON_Y = "y"
        private const val JSON_DELTA = "delta"
        const val TYPE = "mousemove"

        override fun invoke(j: JSONObject): Action {
            return MouseMoveAction(j.getInt(JSON_X), j.getInt(JSON_Y), j.getBoolean(JSON_DELTA))
        }
    }

    override fun perform() {
        if (delta) {
            val pos = MouseInfo.getPointerInfo().location
            robot.mouseMove(pos.x + x, pos.y + y)
        } else {
            robot.mouseMove(x, y)
        }
    }

    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            put(JSON_X, x)
            put(JSON_Y, y)
            put(JSON_DELTA, delta)
        }
    }

}

class MouseWheelAction(amount: Int = 1) : Action() {

    override val type: String = TYPE

    val amountProperty = intProperty(amount)
    var amount by amountProperty

    companion object : ActionCompanion() {
        private const val JSON_AMOUNT = "amount"
        const val TYPE = "mousewheel"

        override fun invoke(j: JSONObject): Action {
            return MouseWheelAction(j.getInt(JSON_AMOUNT))
        }
    }

    override fun perform() {
        robot.mouseWheel(amount)
    }

    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            put(JSON_AMOUNT, amount)
        }
    }

}
