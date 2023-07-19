package iguanastin.deckconfiger.app

import iguanastin.deckconfiger.app.config.DeckConfig
import iguanastin.deckconfiger.view.MainView
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.stage.Stage
import tornadofx.App
import java.io.File
import java.nio.file.Files
import java.nio.file.OpenOption
import kotlin.concurrent.thread

class MyApp: App(MainView::class, Styles::class) {

    companion object {
        const val version = "0.0.1"
    }


    val deckConfigProperty = SimpleObjectProperty<DeckConfig?>()
    var deckConfig: DeckConfig?
        get() = deckConfigProperty.get()
        set(value) = deckConfigProperty.set(value)

    val currentFileProperty = SimpleObjectProperty<File?>()
    var currentFile: File?
        get() = currentFileProperty.get()
        set(value) = currentFileProperty.set(value)

    val unsavedChangesProperty = SimpleBooleanProperty(false)
    var unsavedChanges: Boolean
        get() = unsavedChangesProperty.get()
        set(value) = unsavedChangesProperty.set(value)


    override fun start(stage: Stage) {
        super.start(stage)

        // TODO connect on serial port

        stage.width = 800.0
        stage.height = 600.0
    }

    fun loadConfigFromFile(file: File) {
        deckConfig = DeckConfig.fromFile(file)
        currentFile = file
    }

    fun saveConfigToFile(file: File) {
        val json = deckConfig?.toJSON() ?: return
        Files.writeString(file.toPath(), json.toString())
    }

}