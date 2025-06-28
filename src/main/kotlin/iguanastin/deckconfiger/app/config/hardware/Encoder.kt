package iguanastin.deckconfiger.app.config.hardware

import iguanastin.deckconfiger.app.config.profile.Binding
import iguanastin.deckconfiger.app.config.profile.EncoderBinding
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import org.json.JSONObject
import tornadofx.getValue
import tornadofx.setValue

class Encoder(json: JSONObject? = null, id: Int = -1): HardwareComponent(json, id) {

    val secondaryPinProperty = SimpleIntegerProperty(json?.optInt("pin2") ?: -1)
    var secondaryPin by secondaryPinProperty

    val identLeftProperty = SimpleBooleanProperty()
    var identLeft by identLeftProperty

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

    override fun createBinding(): Binding {
        return EncoderBinding().apply { id = this@Encoder.id }
    }

}