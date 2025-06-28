package iguanastin.deckconfiger.app.config.hardware

import iguanastin.deckconfiger.app.config.profile.Binding
import org.json.JSONObject

class LEDLight(json: JSONObject? = null, id: Int = -1): HardwareComponent(json, id) {

    companion object : ComponentCompanion() {
        const val TYPE = "led"

        override fun fromJSON(j: JSONObject): HardwareComponent {
            return LEDLight(j)
        }
    }

    override val type: String = TYPE

    override fun createBinding(): Binding {
        TODO("Not yet implemented")
    }

}