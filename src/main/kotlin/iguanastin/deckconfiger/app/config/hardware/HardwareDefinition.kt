package iguanastin.deckconfiger.app.config.hardware

import org.json.JSONObject
import tornadofx.*

class HardwareDefinition {

    val components = observableListOf<HardwareComponent>()


    fun toJSON(): JSONObject {
        val json = JSONObject()

        components.forEach { json.append(jsonComponentsName, it.toJSON()) }

        return json
    }

    fun getNextID(): Int {
        return (components.maxOfOrNull { it.id } ?: -1) + 1
    }


    companion object {
        private const val jsonComponentsName = "components"

        fun fromJSON(json: JSONObject): HardwareDefinition {
            return HardwareDefinition().apply {
                json.optJSONArray(jsonComponentsName)?.forEach {
                    components.add(HardwareComponent.fromJSON(it as JSONObject))
                }
            }
        }
    }

}