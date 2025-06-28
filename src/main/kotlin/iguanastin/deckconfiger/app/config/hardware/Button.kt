package iguanastin.deckconfiger.app.config.hardware

import iguanastin.deckconfiger.app.config.profile.Binding
import iguanastin.deckconfiger.app.config.profile.ButtonBinding
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleIntegerProperty
import org.json.JSONObject
import tornadofx.objectProperty

class Button(json: JSONObject? = null, id: Int = -1) : HardwareComponent(json, id) {

    enum class Detect(val value: Int) {
        LOW(0),
        HIGH(1)
    }

    companion object : ComponentCompanion() {
        const val type = "button"
        private const val defaultDebounce = 5
        private val defaultDetect = Detect.LOW

        override fun fromJSON(j: JSONObject): HardwareComponent {
            return Button(j)
        }
    }

    val detectPressProperty =
        objectProperty<Detect>(
            if ((json?.optInt("detect") ?: defaultDetect.value) == Detect.LOW.value) Detect.LOW else Detect.HIGH
        )
    var detectPress: Detect
        get() = detectPressProperty.get()
        set(value) = detectPressProperty.set(value)

    val debounceProperty = SimpleIntegerProperty(json?.optInt("debounce") ?: defaultDebounce)
    var debounceInterval: Int
        get() = debounceProperty.get()
        set(value) = debounceProperty.set(value)

    override val type: String = Button.type


    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            put("detect", detectPress.value)
            put("debounce", debounceInterval)
        }
    }

    override fun createBinding(): Binding {
        return ButtonBinding().apply { id = this@Button.id }
    }

}