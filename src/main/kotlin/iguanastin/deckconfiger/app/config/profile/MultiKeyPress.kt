package iguanastin.deckconfiger.app.config.profile

import org.json.JSONArray
import org.json.JSONObject

class MultiKeyPress(json: JSONObject? = null) : Action() {

    init {
        json?.getJSONArray("events")?.forEach {
            val j = (it as JSONObject)
            events.add(when (j.getString("type")) {
                KeyMultiKeyAction.type -> KeyMultiKeyAction(j)
                DelayMultiKeyAction.type -> DelayMultiKeyAction(j)
                else -> throw IllegalArgumentException("Invalid type: ${j.getString("type")}")
            })
        }
    }

    companion object {
        val type: String = "multi_key_press"
    }

    override val type: String = MultiKeyPress.type

    val events = mutableListOf<MultiKeyAction>()

    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            put("events", JSONArray().apply {
                events.forEach {
                    put(it.toJSON())
                }
            })
        }
    }

}