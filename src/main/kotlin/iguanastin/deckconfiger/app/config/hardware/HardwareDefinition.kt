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

        private val componentJsonFactories: Map<String, (JSONObject) -> HardwareComponent> = mapOf(
            Pair(Button.type) { json -> Button(json) },
            Pair(Encoder.type) { json -> Encoder(json) },
            Pair(LEDLight.type) { json -> LEDLight(json) },
            Pair(RGBLight.type) { json -> RGBLight(json) }
        )

        fun fromJSON(json: JSONObject): HardwareDefinition {
            return HardwareDefinition().apply {
                json.optJSONArray(jsonComponentsName)?.forEach {
                    components.add(componentJsonFactories[(it as JSONObject).getString("type")]?.invoke(it) ?: return@forEach)
                }
            }
        }
    }

}