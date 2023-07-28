package iguanastin.deckconfiger.app

import com.fazecast.jSerialComm.SerialPort
import iguanastin.deckconfiger.app.config.DeckConfig
import iguanastin.deckconfiger.app.config.hardware.Button
import iguanastin.deckconfiger.app.config.hardware.Encoder
import iguanastin.deckconfiger.app.config.profile.DeckProfile
import iguanastin.deckconfiger.app.serial.SerialCommunicator
import iguanastin.deckconfiger.app.serial.SerialMessage
import iguanastin.deckconfiger.view.MainView
import iguanastin.deckconfiger.view.runOnUIThread
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.beans.property.SimpleObjectProperty
import javafx.stage.Stage
import javafx.util.Duration
import org.json.JSONObject
import tornadofx.*
import java.io.File
import java.nio.ByteBuffer
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

    val serial =
        SerialCommunicator(SerialPort.getCommPorts().single { it.portDescription == "Serial/Keyboard/Mouse/Joystick" })


    init {
        serial.messageHandler = { msg: SerialMessage ->
            when (msg.type) {
                SerialMessage.Type.REQUEST_IDENTIFY -> SerialMessage(
                    SerialMessage.Type.RESPOND_IDENTIFY,
                    msg.id,
                    "USBDeck Config Editor ($version): ${System.getProperty("os")}".toByteArray(Charsets.US_ASCII)
                )
                SerialMessage.Type.RESPOND_CONFIG -> {
                    val str = msg.bytesToString(Charsets.US_ASCII)
                    runOnUIThread {
                        deckConfig = if (!str.isNullOrBlank()) DeckConfig.fromJSON(JSONObject(str)) else DeckConfig()
                    }
                    null
                }
                SerialMessage.Type.RESPOND_ERROR -> {
                    println("ERROR RESPONSE (${msg.id}): " + msg.bytesToString(Charsets.US_ASCII))
                    null
                }
                SerialMessage.Type.RESPOND_OK -> {
                    println("Respond OK (${msg.id})")
                    null
                }
                SerialMessage.Type.IDENT_BUTTON -> {
                    handleSerialIdentButton(msg)
                }
                SerialMessage.Type.IDENT_ENCODER -> {
                    handleSerialIdentEncoder(msg)
                }
                else -> null
            }
        }
    }

    private fun handleSerialIdentButton(msg: SerialMessage): SerialMessage {
        val pin = ByteBuffer.wrap(msg.bytes).int
        val button = deckConfig?.hardware?.components?.singleOrNull { it.primaryPin == pin && it is Button }
        button?.ident = true
        Timeline(KeyFrame(Duration.seconds(3.0), {
            button?.ident = false
        })).play()
        return SerialMessage(SerialMessage.Type.RESPOND_OK, msg.id)
    }

    private fun handleSerialIdentEncoder(msg: SerialMessage): SerialMessage {
        val pin = ByteBuffer.wrap(msg.bytes).int
        val encoder =
            deckConfig?.hardware?.components?.singleOrNull { it is Encoder && (it.primaryPin == pin || it.secondaryPin == pin) }
        encoder?.apply {
            ident = true
            (this as Encoder).identLeft = primaryPin == pin
        }
        Timeline(KeyFrame(Duration.seconds(3.0), {
            encoder?.ident = false
        })).play()
        return SerialMessage(SerialMessage.Type.RESPOND_OK, msg.id)
    }


    override fun start(stage: Stage) {
        super.start(stage)

        serial.sendMessage(SerialMessage.Type.REQUEST_CONFIG)

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

    fun syncToDevice(): Boolean {
        val jsonString = deckConfig?.toJSON()?.toString() ?: return false
        val bytes = jsonString.toByteArray(Charsets.US_ASCII)

        if (serial.sendMessage(SerialMessage.Type.CHANGE_CONFIG, bytes) < 0) {
            return false
        } else {
            serial.connected = false
        }

        return true
    }

    fun syncFromDevice() {
        serial.sendMessage(SerialMessage.Type.REQUEST_CONFIG)
    }

    fun identLED(pin: Int) {
        if (pin < 0 || pin > 255) throw IllegalArgumentException("Pin number outside of range: $pin")
        val bytes = pin.toString().toByteArray(Charsets.US_ASCII)
        serial.sendMessage(SerialMessage.Type.IDENT_LED, bytes)
    }

    override fun stop() {
        serial.close()
        super.stop()
    }

}