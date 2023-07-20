package iguanastin.deckconfiger.app.config.hardware

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import org.json.JSONObject

class PushButton(
    primaryPin: Int,
    x: Int,
    y: Int,
    detectPress: DetectPress = DetectPress.LOW,
    debounceInterval: Int = 5
) : HardwareInput(primaryPin, x, y) {

    enum class DetectPress {
        LOW,
        HIGH
    }

    constructor(json: JSONObject) : this(
        json.getInt("pin"),
        json.getInt("x"),
        json.getInt("y"),
        DetectPress.valueOf(json.getString("detect").uppercase()),
        json.getInt("debounce")
    )

    companion object {
        const val type = "pushbutton"
    }

    val detectPressProperty = SimpleObjectProperty(detectPress)
    var detectPress: DetectPress
        get() = detectPressProperty.get()
        set(value) = detectPressProperty.set(value)

    val debounceProperty = SimpleIntegerProperty(debounceInterval)
    var debounceInterval: Int
        get() = debounceProperty.get()
        set(value) = debounceProperty.set(value)

    override val type: String = PushButton.type

    override fun toJSON(): JSONObject {
        return super.toJSON().apply {
            put("detect", detectPress.name)
            put("debounce", debounceInterval)
        }
    }

}