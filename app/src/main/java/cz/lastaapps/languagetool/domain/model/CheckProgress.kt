package cz.lastaapps.languagetool.domain.model

import kotlin.time.Duration

sealed interface CheckProgress {
    @JvmInline
    value class RateLimit(val duration: Duration) : CheckProgress
    object Ready : CheckProgress
    object Processing : CheckProgress
}
