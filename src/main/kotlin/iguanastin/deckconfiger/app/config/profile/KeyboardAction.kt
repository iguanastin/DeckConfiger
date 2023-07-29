package iguanastin.deckconfiger.app.config.profile

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import org.json.JSONObject
import tornadofx.*

class KeyboardAction : Action() {

    override val type: Int = KeyboardAction.type

    val keys = observableListOf<Int>()

    val ctrlProperty = SimpleBooleanProperty()
    var ctrl by ctrlProperty

    val shiftProperty = SimpleBooleanProperty()
    var shift by shiftProperty

    val altProperty = SimpleBooleanProperty()
    var alt by altProperty

    val guiProperty = SimpleBooleanProperty()
    var gui by guiProperty


    val printTextProperty = SimpleStringProperty()
    var printText by printTextProperty


    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            putOpt("print", printText)
            putOpt("ctrl", ctrl)
            putOpt("alt", alt)
            putOpt("shift", shift)
            putOpt("gui", gui)

            var i = 0
            keys.forEach {
                if (i++ > 5) return@forEach
                accumulate("keys", it)
            }
        }
    }

    companion object {
        const val type = KEYBOARD_ACTION

        fun fromJSON(json: JSONObject): KeyboardAction {
            return KeyboardAction().apply {
                json.optJSONArray("keys")?.forEach { keys.add(it as Int) }
                ctrl = json.optBoolean("ctrl")
                alt = json.optBoolean("alt")
                shift = json.optBoolean("shift")
                gui = json.optBoolean("gui")
                printText = json.optString("print")
            }
        }
    }

}