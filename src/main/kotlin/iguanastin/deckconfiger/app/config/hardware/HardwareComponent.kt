package iguanastin.deckconfiger.app.config.hardware

import javafx.beans.property.SimpleIntegerProperty
import org.json.JSONObject

abstract class HardwareComponent(primaryPin: Int) {

    val primaryPinProperty = SimpleIntegerProperty(primaryPin)
    var primaryPin: Int
        get() = primaryPinProperty.get()
        set(value) = primaryPinProperty.set(value)

    abstract val type: String

    open fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("type", type)
            put("pin", primaryPin)
        }
    }

}