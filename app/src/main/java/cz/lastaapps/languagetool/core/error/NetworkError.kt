package cz.lastaapps.languagetool.core.error

sealed interface NetworkError : DomainError {
    data class Timeout(override val throwable: Throwable?) : DomainError
    data class ConnectionClosed(override val throwable: Throwable?) : DomainError
    data class NoInternet(override val throwable: Throwable?) : DomainError
    data class SerializationError(override val throwable: Throwable?) : DomainError
}
