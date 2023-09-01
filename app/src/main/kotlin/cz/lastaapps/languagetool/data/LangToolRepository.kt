package cz.lastaapps.languagetool.data

import cz.lastaapps.languagetool.api.LanguageToolApi
import cz.lastaapps.languagetool.api.model.toDomain
import cz.lastaapps.languagetool.core.Outcome
import cz.lastaapps.languagetool.data.provider.CorrectionConfigProvider
import cz.lastaapps.languagetool.domain.model.Language
import cz.lastaapps.languagetool.domain.model.MatchedText
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal interface LangToolRepository {
    suspend fun correctText(text: String): Outcome<MatchedText>
    suspend fun getLanguages(): Outcome<ImmutableList<Language>>
}

internal class LangToolRepositoryImpl(
    private val api: LanguageToolApi,
    private val correctionConfigProvider: CorrectionConfigProvider,
) : LangToolRepository {
    override suspend fun correctText(text: String): Outcome<MatchedText> =
        api.correct(text, correctionConfigProvider.getCorrectionConfig())
            .map { it.toDomain(text) }

    override suspend fun getLanguages(): Outcome<ImmutableList<Language>> =
        api.getLanguages().map {
            it.map { lang -> lang.toDomain() }.toImmutableList()
        }
}
