package iguanastin.deckconfiger.app.config.hardware

import org.json.JSONObject

class PushButton(primaryPin: Int): HardwareInput(primaryPin) {

    constructor(json: JSONObject): this(json.getInt("pin"))

    companion object {
        const val type = "pushbutton"
    }

    override val type: String = PushButton.type

}