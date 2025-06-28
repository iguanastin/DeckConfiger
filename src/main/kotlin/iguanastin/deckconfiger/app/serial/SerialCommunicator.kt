package iguanastin.deckconfiger.app.serial

import com.fazecast.jSerialComm.SerialPort
import iguanastin.deckconfiger.app.readInt
import javafx.beans.property.SimpleBooleanProperty
import tornadofx.*
import java.nio.ByteBuffer
import kotlin.concurrent.thread

class SerialCommunicator(private val port: SerialPort) {

    val connectedProperty = SimpleBooleanProperty(false)
    var connected by connectedProperty

    private var rollingRequestID: Byte = 1

    @Volatile
    private var closed = false

    var messageHandler: (SerialMessage) -> SerialMessage? = { null }
    var rawSerialHandler: (Byte) -> Unit = {}


    init {

        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 3000, 0)
        thread(isDaemon = true, name = "Serial Read") {
            val one = ByteArray(1)

            while (!closed) {
                if (port.lastErrorCode != 0 || !port.isOpen) {
                    connected = false
                    Thread.sleep(500)
                    port.closePort()
                    if (port.openPort()) connected = true
                }

                if (port.readBytes(one, 1) < 1) continue
                val read = one[0]
                if (read == SerialMessage.SERIAL_MESSAGE_START) {
                    val msg = readMessage()
                    val response = messageHandler.invoke(msg)
                    if (response != null) sendMessage(response)
                } else {
                    rawSerialHandler(read)
                }
            }
        }
    }


    private fun readMessage(): SerialMessage {
        val read = ByteArray(6)
        if (port.readBytes(read, 6) == -1) throw IllegalArgumentException("AHHHHHHHHHHHHHHHHHHHH")
        val type = read[0].toInt()
        val id = read[1].toInt()
        val len = read.readInt(2)
        val bytes = if (len == 0) null else ByteArray(len)
        if (bytes != null) {
            if (port.readBytes(bytes, len.toLong()) == -1) throw IllegalArgumentException("AHHHHHHHHHHHHHHHHHHHH2")
        }

        return SerialMessage(SerialMessage.ordToType[type]!!, id, bytes)
    }

    // Returns ID of sent message, or -1 if write failed
    fun sendMessage(
        type: SerialMessage.Type,
        bytes: ByteArray? = null,
        id: Int = rollingRequestID++.toInt()
    ): Int {
        tryEnsurePortIsOpen()

        val write = ByteArray(7 + (bytes?.size ?: 0))
        write[0] = SerialMessage.SERIAL_MESSAGE_START
        write[1] = type.ordinal.toByte()
        write[2] = id.toByte()
        ByteBuffer.wrap(write, 3, 4).putInt(bytes?.size ?: 0)
        bytes?.copyInto(write, 7)
        port.writeBytes(write, write.size.toLong())

        if (port.lastErrorCode != 0) connected = false

        return if (port.lastErrorCode != 0) -1 else id
    }

    fun sendMessage(msg: SerialMessage): Int {
        return sendMessage(msg.type, msg.bytes, msg.id)
    }

    fun sendRaw(bytes: ByteArray): Int {
        tryEnsurePortIsOpen()
        return port.writeBytes(bytes, bytes.size.toLong())
    }

    private fun tryEnsurePortIsOpen() {
        if (port.lastErrorCode != 0) {
            connected = false
            port.closePort()
            if (port.openPort()) connected = true
        }
    }

    fun close() {
        closed = true
        port.closePort()
    }

}