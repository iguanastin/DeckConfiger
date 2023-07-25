package iguanastin.deckconfiger.app.serial

import com.fazecast.jSerialComm.SerialPort
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import kotlin.concurrent.thread

class SerialCommunicator {

    companion object {
        const val SERIAL_MESSAGE_START: Int = 255
    }

    private var port: SerialPort? = null
        set(value) {
            input = null
            output = null
            field?.closePort()
            field = value
            value?.openPort()
            value?.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0)
        }
    private var input: InputStream? = null
        get() {
            if (field == null) field = port?.inputStream
            return field
        }
        set(value) {
            field?.close()
            field = value
        }
    private var output: OutputStream? = null
        get() {
            if (field == null) field = port?.outputStream
            return field
        }
        set(value) {
            field?.close()
            field = value
        }

    private var rollingRequestID: Byte = 1

    @Volatile private var closed = false

    var messageHandler: (SerialMessage) -> SerialMessage? = { null }


    init {
        thread(isDaemon = true, name = "SerialCommunicator") {
            while (!closed) {
                val read = input?.read()
                if (read == SERIAL_MESSAGE_START) {
                    val msg = readMessage(input!!)
                    val response = messageHandler.invoke(msg)
                    if (response != null) sendMessage(response, output!!)
                } else {
                    // TODO Read generic serial data
                }
            }
        }
    }


    private fun readMessage(input: InputStream): SerialMessage {
        val type = input.read()
        val id = input.read()
        val len = ByteBuffer.wrap(ByteArray(4).apply { input.read(this) }).int
        val bytes = input.readNBytes(len)

        return SerialMessage(SerialMessage.Type.values().single { it.ordinal == type }, id, bytes)
    }

    private fun sendMessage(
        type: SerialMessage.Type,
        output: OutputStream,
        bytes: ByteArray? = null,
        id: Int = rollingRequestID++.toInt()
    ): Int {
        output.apply {
            write(SERIAL_MESSAGE_START)
            write(type.ordinal)
            write(id)
            write(ByteBuffer.allocate(4).putInt(bytes?.size ?: 0).array())
            if (bytes != null) write(bytes)
        }

        return id
    }

    private fun sendMessage(msg: SerialMessage, output: OutputStream): Int {
        return sendMessage(msg.type, output, msg.bytes, msg.id)
    }

    fun discoverPort(): Boolean {
        port = SerialPort.getCommPorts().singleOrNull { it.portDescription == "Serial/Keyboard/Mouse/Joystick" }
        return port != null
    }

    fun close() {
        port = null
    }

}