package iguanastin.deckconfiger.app.config.hardware

import org.json.JSONObject
import tornadofx.*

class HardwareDefinition {

    val components = observableListOf<HardwareComponent>()

    private val ids = mutableMapOf<Int, HardwareComponent>()
    val componentByID: Map<Int, HardwareComponent> = ids

    init {
        components.onChange {
            while (it.next()) {
                it.removed.forEach { ids.remove(it.id) }
                it.addedSubList.forEach { ids.put(it.id, it) }
            }
        }
    }

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