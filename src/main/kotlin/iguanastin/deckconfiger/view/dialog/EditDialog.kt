package iguanastin.deckconfiger.view.dialog

import iguanastin.deckconfiger.app.Styles
import iguanastin.deckconfiger.view.onClick
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import tornadofx.*

class EditDialog(title: String, val fields: List<EditField>, var onAccept: () -> Unit = {}) : StackDialog() {

    val titleProperty = stringProperty(title)
    var title: String by titleProperty

    val acceptTextProperty = stringProperty("Accept")
    var acceptText: String by acceptTextProperty

    val cancelTextProperty = stringProperty("Cancel")
    var cancelText: String by cancelTextProperty

    val acceptListeners = observableListOf<() -> Unit>()

    init {
        root.graphic = initViews()
    }

    private fun initViews(): GridPane = gridpane {
        addClass(Styles.dialogRoot)
        hgap = 10.0
        vgap = 10.0

        addRow(0, Label().apply {
            textProperty().bind(titleProperty)
            addClass(Styles.dialogHeader)
        })

        fields.forEachIndexed { i, field ->
            addRow(
                i + 1,
                Label(field.name),
                field.field
            )
        }

        addRow(rowCount, HBox(5.0).apply {
            gridpaneConstraints { columnSpan = columnCount }
            alignment = Pos.CENTER_RIGHT

            button(acceptTextProperty) {
                onClick {
                    apply()
                    close()
                }
            }
            button(cancelTextProperty) { onClick { close() } }
        })
    }

    private fun apply() {
        fields.forEach { it.apply() }
        acceptListeners.forEach { it() }
        onAccept()
    }

    override fun requestFocus() {
        fields.first().field.requestFocus()
    }

}

