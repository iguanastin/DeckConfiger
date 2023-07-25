package iguanastin.deckconfiger.app.serial

import com.fazecast.jSerialComm.SerialPort
import java.nio.ByteBuffer
import kotlin.concurrent.thread

class SerialCommunicator(private val port: SerialPort) {

    companion object {
        const val SERIAL_MESSAGE_START: Int = 255
    }

    private var rollingRequestID: Byte = 1

    @Volatile
    private var closed = false

    var messageHandler: (SerialMessage) -> SerialMessage? = { null }


    init {
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0)
        if (!port.openPort()) {
            close()
        } else {
//            port.addDataListener(object : SerialPortDataListener {
//                override fun getListeningEvents(): Int {
//                    return SerialPort.LISTENING_EVENT_DATA_RECEIVED
//                }
//
//                override fun serialEvent(p0: SerialPortEvent?) {
//                    println(p0?.receivedData?.joinToString(", "))
//                }
//            })

            thread(isDaemon = true, name = "SerialCommunicator") {
                while (!closed) {
                    val one = ByteArray(1)
                    if (port.readBytes(one, 1) < 1) continue
                    val read = one[0]
                    if (read == SERIAL_MESSAGE_START.toByte()) {
                        val msg = readMessage()
                        val response = messageHandler.invoke(msg)
                        if (response != null) sendMessage(response)
                    } else {
                        print(read.toInt().toChar())
                    }
                }
            }
        }
    }


    private fun readMessage(): SerialMessage {
        val read = ByteArray(6)
        if (port.readBytes(read, 6) == -1) throw IllegalArgumentException("AHHHHHHHHHHHHHHHHHHHH")
        val type = read[0]
        val id = read[1]
        val len = ByteBuffer.wrap(read, 2, 4).int
        val bytes = ByteArray(len)
        if (port.readBytes(bytes, len.toLong()) == -1) throw IllegalArgumentException("AHHHHHHHHHHHHHHHHHHHH2")

        return SerialMessage(SerialMessage.Type.values().single { it.ordinal == type.toInt() }, id.toInt(), bytes)
    }

    fun sendMessage(
        type: SerialMessage.Type,
        bytes: ByteArray? = null,
        id: Int = rollingRequestID++.toInt()
    ): Int {
        val write = ByteArray(7 + (bytes?.size ?: 0))
        write[0] = SERIAL_MESSAGE_START.toByte()
        write[1] = type.ordinal.toByte()
        write[2] = id.toByte()
        ByteBuffer.wrap(write, 3, 4).putInt(bytes?.size ?: 0)
        bytes?.copyInto(write, 7)
        port.writeBytes(write, write.size.toLong())

        return id
    }

    fun sendMessage(msg: SerialMessage): Int {
        return sendMessage(msg.type, msg.bytes, msg.id)
    }

    fun close() {
        closed = true
        port.closePort()
    }

}