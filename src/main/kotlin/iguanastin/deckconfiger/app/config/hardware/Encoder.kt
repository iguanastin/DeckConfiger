package iguanastin.deckconfiger.app.config.hardware

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import org.json.JSONObject

class Encoder(json: JSONObject? = null, id: Int = -1): HardwareInput(json, id) {

    companion object {
        const val type = "encoder"
    }

    val secondaryPinProperty = SimpleIntegerProperty()
    var secondaryPin: Int
        get() = secondaryPinProperty.get()
        set(value) = secondaryPinProperty.set(value)

    val identLeftProperty = SimpleBooleanProperty()
    var identLeft: Boolean
        get() = identLeftProperty.get()
        set(value) = identLeftProperty.set(value)

    override val type: String = Encoder.type

    init {
        secondaryPin = json?.optInt("pin2") ?: 0
    }


    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            put("pin2", secondaryPin)
        }
    }

}