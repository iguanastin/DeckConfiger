package iguanastin.deckconfiger.app.config

import iguanastin.deckconfiger.app.config.hardware.HardwareComponent
import iguanastin.deckconfiger.app.config.hardware.HardwareDefinition
import iguanastin.deckconfiger.app.config.profile.DeckProfile
import org.json.JSONArray
import org.json.JSONObject
import tornadofx.*
import java.io.File
import java.nio.file.Files

class DeckConfig {

    var hardware: HardwareDefinition = HardwareDefinition()

    val profiles = observableListOf<DeckProfile>()


    fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("hardware", hardware.toJSON())
            put("profiles", JSONArray().apply {
                profiles.forEach { put(it.toJSON()) }
            })
        }
    }

    fun getComponent(id: Int): HardwareComponent? {
        return hardware.components.singleOrNull { it.id == id }
    }

    companion object {
        fun fromJSON(json: JSONObject): DeckConfig {
            return DeckConfig().apply {
                hardware = HardwareDefinition.fromJSON(json.getJSONObject("hardware"))
                profiles.clear()
                json.optJSONArray("profiles")?.forEach {
                    profiles.add(DeckProfile.fromJSON(it as JSONObject))
                }
            }
        }

        fun fromFile(file: File): DeckConfig {
            return fromJSON(JSONObject(Files.readString(file.toPath())))
        }
    }

}