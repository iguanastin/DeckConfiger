package iguanastin.deckconfiger.app

fun <T> MutableList<T>.addIfNotContains(t: T) {
    if (!contains(t)) add(t)
}