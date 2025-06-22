package iguanastin.deckconfiger.app.config.profile

import org.json.JSONObject

abstract class BindingCompanion {
    abstract fun fromJSON(j: JSONObject): Binding
}

abstract class Binding(val id: Int) {

    protected abstract val type: String

    companion object : BindingCompanion() {
        private const val JSON_TYPE = "type"
        const val JSON_ID = "id"

        @JvmStatic
        protected val types = mutableMapOf<String, BindingCompanion>()

        override fun fromJSON(j: JSONObject): Binding {
            return types[j.getString(JSON_TYPE)]!!.fromJSON(j)
        }
    }

    open fun toJSON(): JSONObject {
        return JSONObject().apply {
            put(JSON_ID, id)
            put(JSON_TYPE, type)
        }
    }

}

class EncoderBinding(id: Int, var onCW: Action? = null, var onCCW: Action? = null) : Binding(id) {

    override val type: String = EncoderBinding.type

    companion object : BindingCompanion() {
        private const val type = "encoderbinding"
        private const val JSON_CW = "cw"
        private const val JSON_CCW = "ccw"

        init {
            types.put(type, this)
        }

        override fun fromJSON(j: JSONObject): Binding {
            return EncoderBinding(
                j.getInt(JSON_ID),
                j.optJSONObject(JSON_CW)?.let { Action.fromJSON(it) },
                j.optJSONObject(JSON_CCW)?.let { Action.fromJSON(it) })
        }
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

}

class ButtonBinding(id: Int, var onPress: Action? = null, var onRelease: Action? = null) : Binding(id) {

    override val type: String = ButtonBinding.type

    companion object : BindingCompanion() {
        private const val type = "buttonbinding"
        private const val JSON_PRESS = "press"
        private const val JSON_RELEASE = "release"

        init {
            types.put(type, this)
        }

        override fun fromJSON(j: JSONObject): Binding {
            return ButtonBinding(
                j.getInt(JSON_ID),
                j.optJSONObject(JSON_PRESS)?.let { Action.fromJSON(it) },
                j.optJSONObject(JSON_RELEASE)?.let { Action.fromJSON(it) })
        }
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

}