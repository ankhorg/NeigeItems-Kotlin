package pers.neige.neigeitems.utils.function

fun interface TriConsumer<A, B, C> {
    fun accept(a: A, b: B, c: C)
}