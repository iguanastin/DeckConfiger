package iguanastin.deckconfiger.app.serial

import java.nio.charset.Charset

class SerialMessage(val type: Type, val id: Int, val bytes: ByteArray? = null) {

    enum class Type {
        REQUEST_IDENTIFY,
        RESPOND_IDENTIFY,
        REQUEST_CONFIG,
        RESPOND_CONFIG,
        RESPOND_EMPTY,
        RESPOND_ERROR,
        RESPOND_OK,
        CHANGE_CONFIG,
        REQUEST_RESET,
        IDENT_LED,
        IDENT_ENCODER,
        IDENT_BUTTON,
        IDENT_RGB,
        BUTTON_DOWN,
        BUTTON_UP,
        ENCODER_CW,
        ENCODER_CCW,
    }

    companion object {
        const val SERIAL_MESSAGE_START: Byte = -1

        val ordToType = mutableMapOf<Int, Type>()

        init {
            Type.values().forEach { ordToType.put(it.ordinal, it) }
        }
    }

    fun bytesToString(charset: Charset): String? = bytes?.toString(charset)

    override fun toString(): String {
        return "($type, id:$id, len:${bytes?.size ?: 0})"
    }

}