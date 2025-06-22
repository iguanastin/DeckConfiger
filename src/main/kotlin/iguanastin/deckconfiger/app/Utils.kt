package iguanastin.deckconfiger.app

import java.nio.ByteBuffer

fun <T> MutableList<T>.addIfNotContains(t: T) {
    if (!contains(t)) add(t)
}

fun Int.isInRange(min: Int, max: Int): Boolean {
    return this in min until max
}

fun ByteArray.readInt(offset: Int = 0): Int {
    return ByteBuffer.wrap(this, offset, 4).int
}