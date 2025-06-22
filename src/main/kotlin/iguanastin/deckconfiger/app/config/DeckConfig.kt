package iguanastin.deckconfiger.app.config

import iguanastin.deckconfiger.app.config.hardware.HardwareDefinition
import iguanastin.deckconfiger.app.config.profile.DeckProfile
import org.json.JSONArray
import org.json.JSONObject
import tornadofx.*
import java.io.File
import java.nio.file.Files

class DeckConfig() {

    var hardware: HardwareDefinition = HardwareDefinition()

    val profiles = observableListOf<DeckProfile>()


    fun toJSON(): JSONObject {
        return JSONObject().apply {
            put(JSON_VERSION, REQUIRE_VERSION)
            put(JSON_HARDWARE, hardware.toJSON())
            put(JSON_PROFILES, JSONArray().apply {
                profiles.forEach { put(it.toJSON()) }
            })
        }
    }

    companion object {
        private const val JSON_HARDWARE = "hardware"
        private const val JSON_PROFILES = "profiles"
        private const val JSON_VERSION = "version"

        private const val REQUIRE_VERSION = 1

        fun fromJSON(json: JSONObject): DeckConfig {
            val v = json.optInt(JSON_VERSION)
            require(v == REQUIRE_VERSION) { "Invalid JSON version: $v" }
            return DeckConfig().apply {
                hardware = HardwareDefinition.fromJSON(json.getJSONObject(JSON_HARDWARE))
                profiles.clear()
                json.optJSONArray(JSON_PROFILES)?.forEach {
                    profiles.add(DeckProfile.fromJSON(it as JSONObject))
                }
            }
        }

        fun fromFile(file: File): DeckConfig {
            return fromJSON(JSONObject(Files.readString(file.toPath())))
        }
    }

}