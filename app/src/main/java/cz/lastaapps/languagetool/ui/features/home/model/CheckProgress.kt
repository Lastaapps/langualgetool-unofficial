package cz.lastaapps.languagetool.ui.features.home.model

import kotlin.time.Duration

sealed interface CheckProgress {
    data class RateLimit(val remaining: Duration, val total: Duration) : CheckProgress
    object Ready : CheckProgress
    object Processing : CheckProgress
}
