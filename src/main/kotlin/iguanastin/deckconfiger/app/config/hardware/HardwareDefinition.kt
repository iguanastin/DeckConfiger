package iguanastin.deckconfiger.app.config.hardware

import org.json.JSONObject
import java.io.File
import java.nio.file.Files

class HardwareDefinition {

    val inputs = mutableListOf<HardwareInput>()
    val outputs = mutableListOf<HardwareOutput>()


    fun toJSON(): JSONObject {
        val json = JSONObject()

        inputs.forEach { json.append(jsonInputsName, it.toJSON()) }
        outputs.forEach { json.append(jsonOutputsName, it.toJSON()) }

        return json
    }


    companion object {
        private const val jsonInputsName = "inputs"
        private const val jsonOutputsName = "outputs"

        private val inputJsonFactories: Map<String, (JSONObject) -> HardwareInput> = mapOf(
            Pair(PushButton.type) { json -> PushButton(json) },
            Pair(RotaryEncoder.type) { json -> RotaryEncoder(json) }
        )
        private val outputJsonFactories: Map<String, (JSONObject) -> HardwareOutput> = mapOf(
            Pair(LEDLight.type) { json -> LEDLight(json) }
        )

        fun fromJSON(json: JSONObject): HardwareDefinition {
            return HardwareDefinition().apply {
                json.optJSONArray(jsonInputsName)?.forEach {
                    inputs.add(inputJsonFactories[(it as JSONObject).getString("type")]?.invoke(it) ?: return@forEach)
                }
                json.optJSONArray(jsonOutputsName)?.forEach {
                    outputs.add(outputJsonFactories[(it as JSONObject).getString("type")]?.invoke(it) ?: return@forEach)
                }
            }
        }
    }

}