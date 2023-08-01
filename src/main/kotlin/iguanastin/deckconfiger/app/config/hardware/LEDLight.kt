package iguanastin.deckconfiger.app.config.hardware

import org.json.JSONObject

class LEDLight(json: JSONObject? = null, id: Int = -1): HardwareOutput(json, id) {

    companion object {
        const val type = "led"
    }

    override val type: String = LEDLight.type

}