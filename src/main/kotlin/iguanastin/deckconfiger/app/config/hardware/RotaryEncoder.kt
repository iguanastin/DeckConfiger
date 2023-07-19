package iguanastin.deckconfiger.app.config.hardware

import org.json.JSONObject

class RotaryEncoder(primaryPin: Int, val secondaryPin: Int): HardwareInput(primaryPin) {

    constructor(json: JSONObject): this(json.getInt("pin"), json.getInt("pin2"))

    companion object {
        const val type = "rotaryencoder"
    }

    override val type: String = RotaryEncoder.type


    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            put("pin2", secondaryPin)
        }
    }

}