package iguanastin.deckconfiger.app.config.hardware

import org.json.JSONObject

class LEDLight(id: Int, primaryPin: Int, x: Int, y: Int): HardwareOutput(id, primaryPin, x, y) {

    constructor(json: JSONObject): this(json.getInt("id"), json.getInt("pin"), json.getInt("x"), json.getInt("y"))

    companion object {
        const val type = "led"
    }

    override val type: String = LEDLight.type

}