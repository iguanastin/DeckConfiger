package iguanastin.deckconfiger.app.config.hardware

import javafx.beans.property.SimpleIntegerProperty
import org.json.JSONObject

class Button(json: JSONObject? = null, id: Int = -1) : HardwareComponent(json, id) {

    companion object {
        const val type = "button"
        const val defaultDebounce = 5
        const val defaultDetect = 0
    }

    val detectPressProperty = SimpleIntegerProperty(json?.optInt("detect") ?: defaultDetect)
    var detectPress: Int
        get() = detectPressProperty.get()
        set(value) = detectPressProperty.set(value)

    val debounceProperty = SimpleIntegerProperty(json?.optInt("debounce") ?: defaultDebounce)
    var debounceInterval: Int
        get() = debounceProperty.get()
        set(value) = debounceProperty.set(value)

    override val type: String = Button.type


    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            put("detect", detectPress)
            put("debounce", debounceInterval)
        }
    }

}