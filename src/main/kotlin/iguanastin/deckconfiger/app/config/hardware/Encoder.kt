package iguanastin.deckconfiger.app.config.hardware

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import org.json.JSONObject

class Encoder(json: JSONObject? = null, id: Int = -1): HardwareComponent(json, id) {

    val secondaryPinProperty = SimpleIntegerProperty(json?.optInt("pin2") ?: -1)
    var secondaryPin: Int
        get() = secondaryPinProperty.get()
        set(value) = secondaryPinProperty.set(value)

    val identLeftProperty = SimpleBooleanProperty()
    var identLeft: Boolean
        get() = identLeftProperty.get()
        set(value) = identLeftProperty.set(value)

    override val type: String = Encoder.type

    companion object : ComponentCompanion() {
        const val type = "encoder"

        override fun fromJSON(j: JSONObject): HardwareComponent {
            return Encoder(j)
        }
    }


    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            put("pin2", secondaryPin)
        }
    }

}