package iguanastin.deckconfiger.app.config.hardware

import javafx.beans.property.SimpleIntegerProperty
import org.json.JSONObject

class Button(
    id: Int,
    primaryPin: Int,
    x: Int,
    y: Int,
    detectPress: Int = defaultDetect,
    debounceInterval: Int = defaultDebounce
) : HardwareInput(id, primaryPin, x, y) {

    constructor(json: JSONObject) : this(
        json.getInt("id"),
        json.getInt("pin"),
        json.getInt("x"),
        json.getInt("y"),
        json.getInt("detect"),
        json.getInt("debounce")
    )

    companion object {
        const val type = "button"
        const val defaultDebounce = 5
        const val defaultDetect = 0
    }

    val detectPressProperty = SimpleIntegerProperty(detectPress)
    var detectPress: Int
        get() = detectPressProperty.get()
        set(value) = detectPressProperty.set(value)

    val debounceProperty = SimpleIntegerProperty(debounceInterval)
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