package iguanastin.deckconfiger.app.config.hardware

import org.json.JSONObject

class LEDLight(primaryPin: Int, x: Int, y: Int): HardwareOutput(primaryPin, x, y) {

    constructor(json: JSONObject): this(json.getInt("pin"), json.getInt("x"), json.getInt("y"))

    companion object {
        const val type = "led"
    }

    override val type: String = LEDLight.type

}