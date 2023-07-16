package cz.lastaapps.languagetool.ui.features.home

import androidx.compose.runtime.Immutable
import arrow.core.Either
import cz.lastaapps.languagetool.core.StateViewModel
import cz.lastaapps.languagetool.core.VMState
import cz.lastaapps.languagetool.core.error.DomainError
import cz.lastaapps.languagetool.core.launchInVM
import cz.lastaapps.languagetool.core.launchVM
import cz.lastaapps.languagetool.data.AppPreferences
import cz.lastaapps.languagetool.data.LangToolRepository
import cz.lastaapps.languagetool.data.model.toDomain
import cz.lastaapps.languagetool.domain.logic.replace
import cz.lastaapps.languagetool.domain.logic.textDiff
import cz.lastaapps.languagetool.domain.model.Language
import cz.lastaapps.languagetool.domain.model.MatchedError
import cz.lastaapps.languagetool.domain.model.MatchedText
import kotlinx.coroutines.flow.onEach

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
    }

    fun onCheckRequest() = launchVM {
        when (val res = repo.correctText(latestState().matched.text)) {
            is Either.Right ->
                updateState {
                    copy(matched = res.value)
                }

            is Either.Left -> updateState {
                copy(error = res.value)
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
}

@Immutable
internal data class HomeState(
    val matched: MatchedText = MatchedText.empty,
    val error: DomainError? = null,
    val maxChars: Int = 20_000, // TODO
    val isPicky: Boolean = false,
    val language: Language? = null,
) : VMState
