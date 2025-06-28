package iguanastin.deckconfiger.app.config.profile

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import org.json.JSONObject
import tornadofx.*

class DeckProfile(name: String, r: Int = 255, g: Int = 255, b: Int = 255) {


    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty

    val rProperty = SimpleIntegerProperty(r)
    var r by rProperty

    val gProperty = SimpleIntegerProperty(g)
    var g by gProperty

    val bProperty = SimpleIntegerProperty(b)
    var b by bProperty

    val bindings = observableListOf<Binding>()
    private val _idMap = mutableMapOf<Int, Binding>()
    val bindingByID: Map<Int, Binding> = _idMap


    companion object {
        private const val JSON_R = "r"
        private const val JSON_G = "g"
        private const val JSON_B = "b"
        private const val JSON_NAME = "name"
        private const val JSON_BINDINGS = "binds"

        fun fromJSON(json: JSONObject): DeckProfile {
            return DeckProfile(
                json.getString(JSON_NAME),
                json.optInt(JSON_R),
                json.optInt(JSON_G),
                json.optInt(JSON_B)
            ).apply {
                json.optJSONArray(JSON_BINDINGS)?.forEach { bindings.add(Binding.fromJSON(it as JSONObject)) }
            }
        }
    }


    init {
        bindings.onChange { c ->
            while (c.next()) {
                c.removed.forEach { _idMap.remove(it.id) }
                c.addedSubList.forEach {
                    require(it.id !in _idMap)
                    _idMap.put(it.id, it)
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

}