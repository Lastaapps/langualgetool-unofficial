package cz.lastaapps.languagetool.core

import arrow.core.Either
import cz.lastaapps.languagetool.core.error.DomainError

typealias Outcome<T>  = Either<DomainError, T>
