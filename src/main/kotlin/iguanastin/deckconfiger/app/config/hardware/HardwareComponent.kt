package iguanastin.deckconfiger.app.config.hardware

import iguanastin.deckconfiger.app.config.profile.Action
import iguanastin.deckconfiger.app.config.profile.Binding
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import org.json.JSONObject
import tornadofx.getValue
import tornadofx.setValue

abstract class ComponentCompanion {
    abstract fun fromJSON(j: JSONObject): HardwareComponent
}

abstract class HardwareComponent(json: JSONObject? = null, id: Int = -1) {

    val idProperty = SimpleIntegerProperty()
    var id by idProperty

    val nameProperty = SimpleStringProperty("")
    var name: String by nameProperty

    val primaryPinProperty = SimpleIntegerProperty()
    var primaryPin by primaryPinProperty

    val xProperty = SimpleIntegerProperty()
    var x by xProperty

    val yProperty = SimpleIntegerProperty()
    var y by yProperty

    val identProperty = SimpleBooleanProperty(false)
    var ident by identProperty

    protected abstract val type: String

    companion object : ComponentCompanion() {
        private const val JSON_TYPE = "type"
        private const val JSON_ID = "id"
        private const val JSON_NAME = "name"
        private const val JSON_PIN = "pin"
        private const val JSON_X = "x"
        private const val JSON_Y = "y"

        protected val types = mutableMapOf<String, ComponentCompanion>(
            Encoder.type.to(Encoder),
            Button.type.to(Button),
            LEDLight.type.to(LEDLight),
            RGBLight.type.to(RGBLight)
        )

        override fun fromJSON(j: JSONObject): HardwareComponent {
            return types[j.getString(JSON_TYPE)]!!.fromJSON(j)
        }
    }

    init {
        this.id = json?.optInt(JSON_ID) ?: id
        name = json?.optString(JSON_NAME) ?: ""
        primaryPin = json?.optInt(JSON_PIN) ?: 0
        x = json?.optInt(JSON_X) ?: 0
        y = json?.optInt(JSON_Y) ?: 0
    }


    open fun toJSON(): JSONObject {
        return JSONObject().apply {
            put(JSON_ID, id)
            put(JSON_NAME, name)
            put(JSON_TYPE, type)
            put(JSON_PIN, primaryPin)
            put(JSON_X, x)
            put(JSON_Y, y)
        }
    }

    abstract fun createBinding(): Binding

}