package iguanastin.deckconfiger.app.config.hardware

import javafx.beans.property.SimpleIntegerProperty
import org.json.JSONObject

class RGBLight(json: JSONObject? = null, id: Int = -1) : HardwareOutput(json, id) {

    companion object {
        const val type = "rgbled"
    }

    val gPinProperty = SimpleIntegerProperty(json?.optInt("gpin") ?: -1)
    var gPin
        get() = gPinProperty.get()
        set(value) = gPinProperty.set(value)

    val bPinProperty = SimpleIntegerProperty(json?.optInt("bpin") ?: -1)
    var bPin
        get() = bPinProperty.get()
        set(value) = bPinProperty.set(value)

    val rProperty = SimpleIntegerProperty(json?.optInt("r") ?: 0)
    var r
        get() = rProperty.get()
        set(value) = rProperty.set(value)

    val gProperty = SimpleIntegerProperty(json?.optInt("g") ?: 0)
    var g
        get() = gProperty.get()
        set(value) = gProperty.set(value)

    val bProperty = SimpleIntegerProperty(json?.optInt("b") ?: 0)
    var b
        get() = bProperty.get()
        set(value) = bProperty.set(value)

    override val type: String = RGBLight.type

    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            put("gpin", gPin)
            put("bpin", bPin)
            put("r", r)
            put("g", g)
            put("b", b)
        }
    }

}