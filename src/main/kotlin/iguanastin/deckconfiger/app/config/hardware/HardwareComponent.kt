package iguanastin.deckconfiger.app.config.hardware

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import org.json.JSONObject

abstract class HardwareComponent(id: Int, primaryPin: Int, x: Int, y: Int) {

    val idProperty = SimpleIntegerProperty(id)
    var id: Int
        get() = idProperty.get()
        set(value) = idProperty.set(value)

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

    val identProperty = SimpleBooleanProperty(false)
    var ident: Boolean
        get() = identProperty.get()
        set(value) = identProperty.set(value)

    abstract val type: String

    open fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("type", type)
            put("pin", primaryPin)
            put("x", x)
            put("y", y)
        }
    }

}