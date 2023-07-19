package iguanastin.deckconfiger.app.config.hardware

import org.json.JSONObject

class LEDLight(primaryPin: Int): HardwareOutput(primaryPin) {

    constructor(json: JSONObject): this(json.getInt("pin"))

    companion object {
        const val type = "led"
    }

    override val type: String = LEDLight.type

}