package iguanastin.deckconfiger.app

import iguanastin.deckconfiger.app.config.DeckConfig
import iguanastin.deckconfiger.app.config.profile.DeckProfile
import iguanastin.deckconfiger.app.serial.SerialCommunicator
import iguanastin.deckconfiger.app.serial.SerialMessage
import iguanastin.deckconfiger.view.MainView
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.stage.Stage
import org.json.JSONObject
import tornadofx.App
import java.io.File
import java.nio.file.Files

class MyApp : App(MainView::class, Styles::class) {

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

    val serial = SerialCommunicator()


    init {
        serial.messageHandler = { msg: SerialMessage ->
            if (msg.type == SerialMessage.Type.REQUEST_IDENTIFY) SerialMessage(
                SerialMessage.Type.RESPOND_IDENTIFY, msg.id, "USBDeck Config Editor ($version): ${System.getProperty("os")}".toByteArray(
                    Charsets.US_ASCII
                )
            )
            else if (msg.type == SerialMessage.Type.RESPOND_CONFIG) {
                deckConfig = DeckConfig.fromJSON(JSONObject(msg.bytesToString(Charsets.UTF_8)))
            }

            null
        }
    }


    override fun start(stage: Stage) {
        super.start(stage)

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

    fun createNewConfig() {
        deckConfig = DeckConfig().apply {
            profiles.add(DeckProfile("Profile 1"))
        }
    }

}