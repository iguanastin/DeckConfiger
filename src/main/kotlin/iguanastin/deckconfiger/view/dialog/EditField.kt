package iguanastin.deckconfiger.view.dialog

import javafx.beans.property.IntegerProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.Property
import javafx.beans.property.StringProperty
import javafx.scene.Node
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ColorPicker
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.scene.paint.Color


abstract class EditField(val name: String, val prop: Property<*>) {

    abstract val field: Node

    abstract fun apply()

}

class EditStringField(name: String, prop: StringProperty) : EditField(name, prop) {

    override val field = TextField(prop.value)

    override fun apply() {
        (prop as StringProperty).value = field.text
    }

}

class EditIntField(name: String, prop: IntegerProperty, val min: Int = Integer.MIN_VALUE, val max: Int = Integer.MAX_VALUE) : EditField(name, prop) {

    override val field = TextField(prop.value.toString()).apply {
        textFormatter = TextFormatter<String> {
            if (it.controlNewText.matches("-?[0-9]*".toRegex())) it else null
        }
    }

    override fun apply() {
        val v = field.text.toInt()
        require(v >= min && v <= max)
        (prop as IntegerProperty).value = v
    }

}

class EditChoiceField<T>(name: String, prop: ObjectProperty<T>, vararg choices: T) : EditField(name, prop) {

    override val field = ChoiceBox<T>().apply {
        items.addAll(choices)
        value = prop.value
    }

    override fun apply() {
        @Suppress("UNCHECKED_CAST")
        (prop as ObjectProperty<T>).value = field.value
    }

}

class EditColorField(name: String, prop: ObjectProperty<Color>) : EditField(name, prop) {

    override val field = ColorPicker(prop.value)

    override fun apply() {
        @Suppress("UNCHECKED_CAST")
        (prop as ObjectProperty<Color>).value = field.value
    }

}