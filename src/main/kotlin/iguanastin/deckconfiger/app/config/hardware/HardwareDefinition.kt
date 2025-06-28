package iguanastin.deckconfiger.app.config.hardware

import org.json.JSONObject
import tornadofx.*

class HardwareDefinition {

    val components = observableListOf<HardwareComponent>()

    private val ids = mutableMapOf<Int, HardwareComponent>()
    val componentByID: Map<Int, HardwareComponent> = ids

    init {
        components.onChange { change ->
            while (change.next()) {
                change.removed.forEach { ids.remove(it.id) }
                change.addedSubList.forEach { ids.put(it.id, it) }
            }
        }
    }

    fun toJSON(): JSONObject {
        val json = JSONObject()

        components.forEach { json.append(JSON_COMPONENTS, it.toJSON()) }

        return json
    }

    fun getNextID(): Int {
        return (components.maxOfOrNull { it.id } ?: -1) + 1
    }


    companion object {
        private const val JSON_COMPONENTS = "components"

        fun fromJSON(json: JSONObject): HardwareDefinition {
            return HardwareDefinition().apply {
                json.optJSONArray(JSON_COMPONENTS)?.forEach {
                    components.add(HardwareComponent.fromJSON(it as JSONObject))
                }
            }
        }
    }

}