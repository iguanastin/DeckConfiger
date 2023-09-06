package iguanastin.deckconfiger.app

import com.fazecast.jSerialComm.SerialPort
import iguanastin.deckconfiger.app.config.DeckConfig
import iguanastin.deckconfiger.app.config.hardware.Button
import iguanastin.deckconfiger.app.config.hardware.Encoder
import iguanastin.deckconfiger.app.config.hardware.RGBLight
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

    lateinit var root: MainView


    init {
        serial.messageHandler = { msg: SerialMessage -> handleSerialMessage(msg) }
    }


    override fun start(stage: Stage) {
        super.start(stage)
        root = find(primaryView) as MainView

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

    fun identRGB(led: RGBLight) {
        val r = led.primaryPin
        val g = led.gPin
        val b = led.bPin

        if (r < 0 || r > 255) throw IllegalArgumentException("Pin number outside of range: $r")
        if (g < 0 || g > 255) throw IllegalArgumentException("Pin number outside of range: $g")
        if (b < 0 || b > 255) throw IllegalArgumentException("Pin number outside of range: $b")

        val bytes = ByteArray(12)
        bytes[0] = (r.toLong() and 0xff000000 shr 24).toByte()
        bytes[1] = (r.toLong() and 0x00ff0000 shr 16).toByte()
        bytes[2] = (r.toLong() and 0x0000ff00 shr 8).toByte()
        bytes[3] = (r.toLong() and 0x000000ff).toByte()

        bytes[4] = (g.toLong() and 0xff000000 shr 24).toByte()
        bytes[5] = (g.toLong() and 0x00ff0000 shr 16).toByte()
        bytes[6] = (g.toLong() and 0x0000ff00 shr 8).toByte()
        bytes[7] = (g.toLong() and 0x000000ff).toByte()

        bytes[8] = (b.toLong() and 0xff000000 shr 24).toByte()
        bytes[9] = (b.toLong() and 0x00ff0000 shr 16).toByte()
        bytes[10] = (b.toLong() and 0x0000ff00 shr 8).toByte()
        bytes[11] = (b.toLong() and 0x000000ff).toByte()

        serial.sendMessage(SerialMessage.Type.IDENT_RGB, bytes)
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
            (this as Encoder).identLeft = primaryPin == pin
            ident = true
        }
        Timeline(KeyFrame(Duration.seconds(3.0), {
            encoder?.ident = false
        })).play()
        return SerialMessage(SerialMessage.Type.RESPOND_OK, msg.id)
    }

    private fun handleSerialMessage(msg: SerialMessage) = when (msg.type) {
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

    override fun stop() {
        serial.close()
        super.stop()
    }

}