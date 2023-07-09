package cz.lastaapps.languagetool.ui.util

infix fun Int.withLength(length: Int) =
    let { offset -> IntRange(offset, offset + length - 1) }

infix fun IntRange.moveBy(offset: Int) =
    IntRange(first + offset, last + offset)

fun IntRange.Companion.fromLength(offset: Int, length: Int) =
    IntRange(offset, offset + length)

val IntRange.sizeAsExclusive: Int
    get() = last - first + 1
