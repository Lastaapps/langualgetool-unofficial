package cz.lastaapps.languagetool.core.util

import arrow.core.Either
import cz.lastaapps.languagetool.core.Outcome
import cz.lastaapps.languagetool.core.error.DomainError
import cz.lastaapps.languagetool.core.error.NetworkError

suspend fun <T> catchingNetwork(block: suspend () -> T): Outcome<T> =
    Either.catch { block() }.mapLeft {
        it.printStackTrace()
        // logging("catchingNetwork").e(it) { "Failed network call" }

        when (it::class.simpleName) {
            "TimeoutException",
            -> NetworkError.Timeout(it)

            "ConnectException",
            -> NetworkError.ConnectionClosed(it)

            "UnknownHostException",
            "NoRouteToHostException",
            "IOException",
            "SSLException",
            "SocketException",
            "HttpRequestTimeoutException",
            "SocketTimeoutException",
            -> NetworkError.NoInternet(it)

            "JsonConvertException",
            "JsonDecodingException",
            -> NetworkError.SerializationError(it)

            else -> DomainError.Unknown(it)
        }
    }
