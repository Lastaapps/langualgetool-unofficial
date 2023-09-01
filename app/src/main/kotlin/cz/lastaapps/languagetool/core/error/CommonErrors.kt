package cz.lastaapps.languagetool.core.error

sealed interface CommonErrors : DomainError {
    object ClipboardEmpty : CommonErrors {
        override val throwable: Throwable? = null
    }

    object TextToLong : CommonErrors {
        override val throwable: Throwable? = null
    }
}
