package iguanastin.deckconfiger.app.config.profile

import javafx.beans.property.ListProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import org.json.JSONArray
import org.json.JSONObject
import tornadofx.*

class DeckProfile(name: String, r: Int = 255, g: Int = 255, b: Int = 255) {


    val nameProperty = SimpleStringProperty(name)
    var name: String
        get() = nameProperty.get()
        set(value) = nameProperty.set(value)

    val rProperty = SimpleIntegerProperty(r)
    var r: Int
        get() = rProperty.get()
        set(value) = rProperty.set(value)

    val gProperty = SimpleIntegerProperty(g)
    var g: Int
        get() = gProperty.get()
        set(value) = gProperty.set(value)

    val bProperty = SimpleIntegerProperty(b)
    var b: Int
        get() = bProperty.get()
        set(value) = bProperty.set(value)

    val bindings = observableListOf<Binding>()
    val idMap = mutableMapOf<Int, Binding>()


    init {
        bindings.onChange { c ->
            while (c.next()) {
                c.removed.forEach { idMap.remove(it.id) }
                c.addedSubList.forEach {
                    require(it.id !in idMap)
                    idMap.put(it.id, it)
                }
            }
        }
    }


    fun toJSON(): JSONObject {
        return JSONObject().apply {
            put(JSON_NAME, name)
            put(JSON_R, r)
            put(JSON_G, g)
            put(JSON_B, b)
            put(JSON_BINDINGS, bindings.map { it.toJSON() })
        }
    }

    override fun toString(): String {
        return name
    }

    companion object {
        private const val JSON_R = "r"
        private const val JSON_G = "g"
        private const val JSON_B = "b"
        private const val JSON_NAME = "name"
        private const val JSON_BINDINGS = "binds"

        fun fromJSON(json: JSONObject): DeckProfile {
            return DeckProfile(json.getString(JSON_NAME), json.optInt(JSON_R), json.optInt(JSON_G), json.optInt(JSON_B)).apply {
                json.optJSONArray(JSON_BINDINGS)?.forEach { bindings.add(Binding.fromJSON(it as JSONObject)) }
            }
        }
    }

}