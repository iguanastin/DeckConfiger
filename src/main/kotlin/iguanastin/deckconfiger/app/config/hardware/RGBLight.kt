package iguanastin.deckconfiger.app.config.hardware

import iguanastin.deckconfiger.app.config.profile.Binding
import javafx.beans.property.SimpleIntegerProperty
import org.json.JSONObject
import tornadofx.getValue
import tornadofx.setValue

class RGBLight(json: JSONObject? = null, id: Int = -1) : HardwareComponent(json, id) {

    companion object : ComponentCompanion() {
        const val type = "rgbled"

        override fun fromJSON(j: JSONObject): HardwareComponent {
            return RGBLight(j)
        }
    }

    val gPinProperty = SimpleIntegerProperty(json?.optInt("gpin") ?: -1)
    var gPin by gPinProperty

    val bPinProperty = SimpleIntegerProperty(json?.optInt("bpin") ?: -1)
    var bPin by bPinProperty

    val rProperty = SimpleIntegerProperty(json?.optInt("r") ?: 0)
    var r by rProperty

    val gProperty = SimpleIntegerProperty(json?.optInt("g") ?: 0)
    var g by gProperty

    val bProperty = SimpleIntegerProperty(json?.optInt("b") ?: 0)
    var b by bProperty

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

    override fun createBinding(): Binding {
        TODO("Not yet implemented")
    }

}