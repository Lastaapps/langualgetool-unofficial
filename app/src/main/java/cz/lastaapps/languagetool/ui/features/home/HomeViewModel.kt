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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

        appPreferences.getApiCredentials().onEach {
            updateState {
                copy(
                    maxChars = it?.let { MAX_CHARS_PREMIUM } ?: MAX_CHARS_FREE,
                    timeout = it?.let { TIMEOUT_PREMIUM } ?: TIMEOUT_FREE,
                )
            }
        }
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
                    copy(matched = res.value)
                }
                launchRateLimitJob()
            }

            is Either.Left -> updateState {
                copy(error = res.value, progress = CheckProgress.Ready)
            }
        }
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
