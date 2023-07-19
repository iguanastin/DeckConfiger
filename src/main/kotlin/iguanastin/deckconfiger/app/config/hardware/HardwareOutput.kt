package iguanastin.deckconfiger.app.config.hardware

import org.json.JSONObject

abstract class HardwareOutput(var primaryPin: Int) {

    abstract val type: String

    open fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("type", type)
            put("pin", primaryPin)
        }
    }
    
}