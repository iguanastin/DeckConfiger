package iguanastin.deckconfiger.app

fun <T> MutableList<T>.addIfNotContains(t: T) {
    if (!contains(t)) add(t)
}

fun Int.isInRange(min: Int, max: Int): Boolean {
    return this in min until max
}