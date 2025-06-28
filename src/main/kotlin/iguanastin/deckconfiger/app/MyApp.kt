package iguanastin.deckconfiger.app

import com.fazecast.jSerialComm.SerialPort
import iguanastin.deckconfiger.app.config.DeckConfig
import iguanastin.deckconfiger.app.config.hardware.RGBLight
import iguanastin.deckconfiger.app.config.profile.ButtonBinding
import iguanastin.deckconfiger.app.config.profile.DeckProfile
import iguanastin.deckconfiger.app.config.profile.EncoderBinding
import iguanastin.deckconfiger.app.serial.SerialCommunicator
import iguanastin.deckconfiger.app.serial.SerialMessage
import iguanastin.deckconfiger.view.MainView
import iguanastin.deckconfiger.view.dialog.StackDialog
import iguanastin.deckconfiger.view.runOnUIThread
import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import javafx.stage.Stage
import org.json.JSONObject
import tornadofx.*
import java.io.File
import java.nio.file.Files
import javax.imageio.ImageIO

class MyApp : App(MainView::class, Styles::class) {

    companion object {
        const val VERSION = "0.1.0" // Update version in pom.xml as well
    }


    val deckConfigProperty = SimpleObjectProperty<DeckConfig?>()
    var deckConfig by deckConfigProperty

    val profileProperty = SimpleObjectProperty<DeckProfile>()
    var profile: DeckProfile? by profileProperty

    val currentFileProperty = SimpleObjectProperty<File?>()
    var currentFile by currentFileProperty

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

        initTrayIcon(stage)

        stage.onCloseRequest = EventHandler {
            it.consume()
            stage.hide()
        }
    }

    private fun initTrayIcon(stage: Stage) {
        fun show() {
            runOnUIThread {
                stage.show()
                stage.isIconified = false
                stage.requestFocus()
            }
        }
        trayicon(ImageIO.read(javaClass.getResource("/imgs/img.png")), "DeckConfiger") {
            addActionListener { show() }
            menu("??????") {
                item("Open") { setOnAction { show() } }
                item("Exit") { setOnAction { runOnUIThread { Platform.exit() } } }
            }
        }
    }

    fun dialog(d: StackDialog) {
        root.root.add(d)
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
        val bytes = ByteArray(4)
        bytes[0] = (pin.toLong() and 0xff000000 shr 24).toByte()
        bytes[1] = (pin.toLong() and 0x00ff0000 shr 16).toByte()
        bytes[2] = (pin.toLong() and 0x0000ff00 shr 8).toByte()
        bytes[3] = (pin.toLong() and 0x000000ff).toByte()
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

    private fun handleSerialMessage(msg: SerialMessage): SerialMessage? {
        when (msg.type) {
            SerialMessage.Type.REQUEST_IDENTIFY -> {
                return SerialMessage(
                    SerialMessage.Type.RESPOND_IDENTIFY,
                    msg.id,
                    "USBDeck Config Editor ($VERSION): ${System.getProperty("os")}".toByteArray(Charsets.US_ASCII)
                )
            }
            SerialMessage.Type.RESPOND_CONFIG -> {
                runOnUIThread {
                    val str = msg.bytesToString(Charsets.US_ASCII)
                    deckConfig = if (!str.isNullOrBlank()) DeckConfig.fromJSON(JSONObject(str)) else DeckConfig()
                }
            }
            SerialMessage.Type.RESPOND_ERROR -> {
                println("ERROR RESPONSE (${msg.id}): " + msg.bytesToString(Charsets.US_ASCII))
            }
            SerialMessage.Type.RESPOND_OK -> {
                println("Responded OK (${msg.id})")
            }
            SerialMessage.Type.BUTTON_DOWN -> {
                (profile?.bindingByID?.getOrDefault(msg.bytes!!.readInt(0), null) as ButtonBinding?)?.press()
            }
            SerialMessage.Type.BUTTON_UP -> {
                (profile?.bindingByID?.getOrDefault(msg.bytes!!.readInt(0), null) as ButtonBinding?)?.release()
            }
            SerialMessage.Type.ENCODER_CW -> {
                (profile?.bindingByID?.getOrDefault(msg.bytes!!.readInt(0), null) as EncoderBinding?)?.cw()
            }
            SerialMessage.Type.ENCODER_CCW -> {
                (profile?.bindingByID?.getOrDefault(msg.bytes!!.readInt(0), null) as EncoderBinding?)?.ccw()
            }

            SerialMessage.Type.RESPOND_IDENTIFY -> TODO()
            SerialMessage.Type.REQUEST_CONFIG -> TODO()
            SerialMessage.Type.RESPOND_EMPTY -> TODO()
            SerialMessage.Type.CHANGE_CONFIG -> TODO()
            SerialMessage.Type.REQUEST_RESET -> TODO()
            SerialMessage.Type.IDENT_LED -> TODO()
            SerialMessage.Type.IDENT_ENCODER -> TODO()
            SerialMessage.Type.IDENT_BUTTON -> TODO()
            SerialMessage.Type.IDENT_RGB -> TODO()
        }

        return null
    }

    override fun stop() {
        serial.close()
        super.stop()
    }

}

fun main(vararg args: String) {
    launch<MyApp>(*args)
}