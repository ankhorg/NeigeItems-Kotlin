package pers.neige.neigeitems.utils.function

fun interface TriFunction<A, B, C, R> {
    fun apply(a: A, b: B, c: C): R
}