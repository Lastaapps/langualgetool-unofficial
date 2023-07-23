package cz.lastaapps.languagetool.ui.features.home

import androidx.compose.runtime.Immutable
import arrow.core.Either
import cz.lastaapps.languagetool.core.StateViewModel
import cz.lastaapps.languagetool.core.VMState
import cz.lastaapps.languagetool.core.error.CommonErrors
import cz.lastaapps.languagetool.core.error.DomainError
import cz.lastaapps.languagetool.core.launchInVM
import cz.lastaapps.languagetool.core.launchVM
import cz.lastaapps.languagetool.core.launchVMJob
import cz.lastaapps.languagetool.data.AppPreferences
import cz.lastaapps.languagetool.data.LangToolRepository
import cz.lastaapps.languagetool.data.getApiCredentials
import cz.lastaapps.languagetool.data.model.toDomain
import cz.lastaapps.languagetool.domain.logic.replace
import cz.lastaapps.languagetool.domain.logic.textDiff
import cz.lastaapps.languagetool.domain.model.CheckProgress
import cz.lastaapps.languagetool.domain.model.Language
import cz.lastaapps.languagetool.domain.model.MatchedError
import cz.lastaapps.languagetool.domain.model.MatchedText
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


private const val MAX_CHARS_FREE = 20_000
private const val MAX_CHARS_PREMIUM = 60_000
private val TIMEOUT_FREE = 6.seconds
private val TIMEOUT_PREMIUM = 1.seconds

internal class HomeViewModel(
    private val repo: LangToolRepository,
    private val appPreferences: AppPreferences,
) : StateViewModel<HomeState>(HomeState()) {

    fun onAppear() = launchOnlyOnce {
        appPreferences.getPicky().onEach {
            updateState { copy(isPicky = it) }
        }.launchInVM()

        appPreferences.getLanguage().onEach {
            updateState { copy(language = it?.toDomain()) }
        }.launchInVM()

        combine(
            appPreferences.getApiUrl(),
            appPreferences.getApiCredentials(),
        ) { url, credentials ->
            val canUsePremium = url != null || credentials != null
            updateState {
                copy(
                    maxChars = if (canUsePremium) {
                        MAX_CHARS_PREMIUM
                    } else {
                        MAX_CHARS_FREE
                    },
                    timeout = if (canUsePremium) {
                        TIMEOUT_PREMIUM
                    } else {
                        TIMEOUT_FREE
                    },
                )
            }
        }.launchInVM()
    }

    fun onCheckRequest() = launchVM {
        val state = latestState()
        val text = state.matched.text
        val maxChars = state.maxChars

        if (state.progress != CheckProgress.Ready) {
            return@launchVM
        }
        if (text.length > maxChars) {
            updateState { copy(error = CommonErrors.TextToLong) }
            return@launchVM
        }

        updateState { copy(progress = CheckProgress.Processing) }
        when (val res = repo.correctText(text)) {
            is Either.Right -> {
                updateState {
                    val oldErrors = matched.errors
                    val newErrors = res.value.errors
                    val merged = mergeSkipped(oldErrors, newErrors).toPersistentList()
                    copy(matched = res.value.copy(errors = merged))
                }
                launchRateLimitJob()
            }

            is Either.Left -> updateState {
                copy(error = res.value, progress = CheckProgress.Ready)
            }
        }
    }

    private fun mergeSkipped(old: List<MatchedError>, new: List<MatchedError>): List<MatchedError> {
        var iO = 0
        var iN = 0
        val out = mutableListOf<MatchedError>()

        while (iO < old.size && iN < new.size) {
            val fO = old[iO]
            val fN = new[iN]

            when {
                fO.range.first < fN.range.first -> iO++
                fO.range.first > fN.range.first -> {
                    iN++
                    out += fN
                }

                fO.range.first == fN.range.first -> {
                    iO++
                    iN++

                    out += if (fO.isSkipped) {
                        fN.copy(isSkipped = true)
                    } else {
                        fN
                    }
                }
            }
        }
        while (iN < new.size) {
            out += new[iN++]
        }

        return out
    }

    fun onTextChanged(newText: String) {
        updateState {

            val currentMatched = matched
            val currentText = currentMatched.text

            val diff = textDiff(currentText, newText)

            copy(
                matched = currentMatched.replace(diff.first, diff.second),
            )
        }
    }

    fun applySuggestion(error: MatchedError, suggestion: String) {
        updateState {
            copy(
                matched = matched.replace(error.range, suggestion),
            )
        }
        latestState().matched.errors.takeIf { it.isEmpty() }?.let {
            onCheckRequest()
        }
    }

    fun skipSuggestion(matchedError: MatchedError) {
        updateState {
            matched.errors.indexOf(matchedError)
                .takeIf { it >= 0 }
                ?.let { errorIndex ->
                    copy(
                        matched = matched.copy(
                            errors = matched.errors
                                .set(errorIndex, matchedError.copy(isSkipped = true)),
                        ),
                    )
                } ?: this
        }
    }

    fun dismissError() {
        updateState { copy(error = null) }
    }

    fun setIsPicky(value: Boolean) = launchVM {
        updateState { copy(isPicky = value) }
        appPreferences.setPicky(value)
    }

    private var rateLimitJob: Job? = null
    private fun launchRateLimitJob() {
        rateLimitJob?.cancel()
        val state = latestState()

        rateLimitJob = launchVMJob {
            val timeout = state.timeout
            updateState { copy(progress = CheckProgress.RateLimit(timeout)) }
            delay(timeout)
            updateState { copy(progress = CheckProgress.Ready) }
        }
    }
}

@Immutable
internal data class HomeState(
    val progress: CheckProgress = CheckProgress.Ready,
    val matched: MatchedText = MatchedText.empty,
    val error: DomainError? = null,
    val maxChars: Int = MAX_CHARS_FREE,
    val timeout: Duration = TIMEOUT_FREE,
    val isPicky: Boolean = false,
    val language: Language? = null,
) : VMState
