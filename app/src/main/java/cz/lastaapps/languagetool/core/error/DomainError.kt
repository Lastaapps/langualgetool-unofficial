package cz.lastaapps.languagetool.core.error

sealed interface DomainError {
    val throwable: Throwable?

    data class Unknown(override val throwable: Throwable?) : DomainError
}

fun DomainError.getMessage(): String = "TODO" // TODO

