package iguanastin.deckconfiger.app.config.hardware

import javafx.beans.property.SimpleIntegerProperty
import org.json.JSONObject

abstract class HardwareComponent(primaryPin: Int, x: Int, y: Int) {

    val primaryPinProperty = SimpleIntegerProperty(primaryPin)
    var primaryPin: Int
        get() = primaryPinProperty.get()
        set(value) = primaryPinProperty.set(value)

    val xProperty = SimpleIntegerProperty(x)
    var x: Int
        get() = xProperty.get()
        set(value) = xProperty.set(value)

    val yProperty = SimpleIntegerProperty(y)
    var y: Int
        get() = yProperty.get()
        set(value) = yProperty.set(value)

    abstract val type: String

    open fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("type", type)
            put("pin", primaryPin)
            put("x", x)
            put("y", y)
        }
    }

}