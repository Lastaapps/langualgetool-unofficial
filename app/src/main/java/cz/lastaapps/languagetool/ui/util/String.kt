package cz.lastaapps.languagetool.ui.util

fun String.takeIfNotBlank(): String? =
    takeIf { it.isNotBlank() }
