package cz.lastaapps.languagetool.ui.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.currentComposer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import cz.lastaapps.languagetool.data.LangToolRepository
import cz.lastaapps.languagetool.data.logic.replace
import cz.lastaapps.languagetool.data.logic.textDiff
import cz.lastaapps.languagetool.data.model.MatchedError
import cz.lastaapps.languagetool.data.model.MatchedText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class HomeViewModel(
    private val repo: LangToolRepository,
) : ViewModel() {

    private val state = MutableStateFlow(HomeState())
    fun getState() = state.asStateFlow()

    fun onCheckRequest() = viewModelScope.launch {
        when (val res = repo.correctText(state.value.matched.text)) {
            is Either.Right -> {
                state.update {
                    it.copy(
                        matched = res.value,
                    )
                }
            }

            is Either.Left -> {
                // TODO("Report error $res")
            }
        }
    }.let { true }

    fun onTextChanged(newText: String) {
        state.update {

            val currentMatched = it.matched
            val currentText = currentMatched.text

            val diff = textDiff(currentText, newText)

            it.copy(
                matched = currentMatched.replace(diff.first, diff.second),
            )
        }
    }

    fun applySuggestion(error: MatchedError, suggestion: String) {
        state.update {
            val currentMatched = it.matched
            it.copy(
                matched = currentMatched.replace(error.range, suggestion),
            )
        }
    }

}

@Immutable
internal data class HomeState(
    val matched: MatchedText = MatchedText.empty,
)
