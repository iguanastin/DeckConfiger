package iguanastin.deckconfiger.app.config.hardware

import org.json.JSONObject

abstract class HardwareOutput(json: JSONObject? = null, id: Int = -1): HardwareComponent(json, id)