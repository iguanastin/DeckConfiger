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


    companion object {
        private const val jsonComponentsName = "components"

        private val componentJsonFactories: Map<String, (JSONObject) -> HardwareComponent> = mapOf(
            Pair(PushButton.type) { json -> PushButton(json) },
            Pair(RotaryEncoder.type) { json -> RotaryEncoder(json) },
            Pair(LEDLight.type) { json -> LEDLight(json) }
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