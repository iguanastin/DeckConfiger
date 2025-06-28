package iguanastin.deckconfiger.app.config.profile

import org.json.JSONObject
import tornadofx.*

abstract class BindingCompanion {
    abstract fun fromJSON(j: JSONObject): Binding
}

abstract class Binding(id: Int = -1) {

    val idProperty = intProperty(id)
    var id by idProperty

    val nameProperty = stringProperty("Unnamed")
    var name: String by nameProperty

    abstract val type: String

    companion object : BindingCompanion() {
        @JvmStatic
        protected val JSON_NAME = "name"
        private const val JSON_TYPE = "type"

        @JvmStatic
        protected val JSON_ID = "id"

        @JvmStatic
        protected val types = mapOf<String, BindingCompanion>(
            EncoderBinding.TYPE.to(EncoderBinding),
            ButtonBinding.TYPE.to(ButtonBinding),
        )

        override fun fromJSON(j: JSONObject): Binding = types[j.getString(JSON_TYPE)]!!.fromJSON(j)
    }

    open fun toJSON(): JSONObject {
        return JSONObject().apply {
            put(JSON_ID, id)
            put(JSON_TYPE, type)
            put(JSON_NAME, name)
        }
    }

    open fun fromJSON(j: JSONObject): Binding {
        id = j.getInt(JSON_ID)
        name = j.getString(JSON_NAME)
        return this
    }

}

class EncoderBinding(var onCW: Action? = null, var onCCW: Action? = null) : Binding() {

    override val type: String = TYPE

    companion object : BindingCompanion() {
        const val TYPE = "encoder"
        private const val JSON_CW = "cw"
        private const val JSON_CCW = "ccw"

        override fun fromJSON(j: JSONObject): Binding = EncoderBinding().fromJSON(j)
    }

    fun cw() {
        onCW?.perform()
    }

    fun ccw() {
        onCCW?.perform()
    }

    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            onCW?.also { put(JSON_CW, it.toJSON()) }
            onCCW?.also { put(JSON_CCW, it.toJSON()) }
        }
    }

    override fun fromJSON(j: JSONObject): Binding {
        onCW = j.optJSONObject(JSON_CW)?.let { Action(it) }
        onCCW = j.optJSONObject(JSON_CCW)?.let { Action(it) }

        return super.fromJSON(j)
    }

}

class ButtonBinding(var onPress: Action? = null, var onRelease: Action? = null) : Binding() {

    override val type: String = TYPE

    companion object : BindingCompanion() {
        const val TYPE = "button"
        private const val JSON_PRESS = "press"
        private const val JSON_RELEASE = "release"

        override fun fromJSON(j: JSONObject): Binding = ButtonBinding().fromJSON(j)
    }

    fun press() {
        onPress?.perform()
    }

    fun release() {
        onRelease?.perform()
    }

    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            onPress?.also { put(JSON_PRESS, it.toJSON()) }
            onRelease?.also { put(JSON_RELEASE, it.toJSON()) }
        }
    }

    override fun fromJSON(j: JSONObject): Binding {
        onPress = j.optJSONObject(JSON_PRESS)?.let { Action(it) }
        onRelease = j.optJSONObject(JSON_RELEASE)?.let { Action(it) }

        return super.fromJSON(j)
    }

}