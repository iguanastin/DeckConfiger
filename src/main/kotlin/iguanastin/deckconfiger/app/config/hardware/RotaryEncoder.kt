package iguanastin.deckconfiger.app.config.hardware

import javafx.beans.property.SimpleIntegerProperty
import org.json.JSONObject

class RotaryEncoder(primaryPin: Int, secondaryPin: Int): HardwareInput(primaryPin) {

    constructor(json: JSONObject): this(json.getInt("pin"), json.getInt("pin2"))

    companion object {
        const val type = "rotaryencoder"
    }

    val secondaryPinProperty = SimpleIntegerProperty(secondaryPin)
    var secondaryPin: Int
        get() = secondaryPinProperty.get()
        set(value) = secondaryPinProperty.set(value)

    override val type: String = RotaryEncoder.type


    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            put("pin2", secondaryPin)
        }
    }

}