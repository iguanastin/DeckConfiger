package iguanastin.deckconfiger.app.config.hardware

import org.json.JSONObject

class LEDLight(json: JSONObject? = null, id: Int = -1): HardwareComponent(json, id) {

    companion object : ComponentCompanion() {
        const val type = "led"

        override fun fromJSON(j: JSONObject): HardwareComponent {
            return LEDLight(j)
        }
    }

    override val type: String = LEDLight.type

}