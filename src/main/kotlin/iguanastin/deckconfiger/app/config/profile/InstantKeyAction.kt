package iguanastin.deckconfiger.app.config.profile

import javafx.beans.property.SimpleIntegerProperty
import org.json.JSONObject
import tornadofx.*

class InstantKeyAction : Action() {

    override val type: Int = InstantKeyAction.type

    val keyProperty = SimpleIntegerProperty()
    var key by keyProperty


    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            putOpt(JSON_KEY, key)
        }
    }

    companion object {
        const val type = INSTANT_KEY_ACTION

        const val JSON_KEY = "key"

        fun fromJSON(json: JSONObject): InstantKeyAction {
            return InstantKeyAction().apply {
                key = json.optInt(JSON_KEY)
            }
        }
    }

}