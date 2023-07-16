package cz.lastaapps.languagetool.data

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import arrow.core.raise.nullable
import cz.lastaapps.languagetool.data.model.LanguageStoreModel
import cz.lastaapps.languagetool.data.model.toDomain
import cz.lastaapps.languagetool.domain.model.ApiCredentials
import cz.lastaapps.languagetool.domain.model.CorrectionConfig
import cz.lastaapps.languagetool.ui.util.takeIfNotBlank
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal interface AppPreferences {
    fun getPicky(): Flow<Boolean>
    suspend fun setPicky(value: Boolean)

    fun getLanguage(): Flow<LanguageStoreModel?>
    suspend fun setLanguage(value: LanguageStoreModel?)

    fun getUsername(): Flow<String?>
    suspend fun setUsername(value: String)

    fun getApiKey(): Flow<String?>
    suspend fun setApiKey(value: String)

    fun getApiUrl(): Flow<String?>
    suspend fun setApiUrl(value: String)
}

internal class AppPreferencesImpl(
    context: Context,
) : AppPreferences {
    private val store = context.store
    private inline val data
        get() = store.data
    private val json = Json

    private suspend fun edit(
        transform: suspend (MutablePreferences) -> Unit
    ) = store.edit(transform).let { }

    override fun getPicky(): Flow<Boolean> =
        data.map { it[KEY_PICKY] }.map { it == "picky" }
            .distinctUntilChanged()

    override suspend fun setPicky(value: Boolean) = edit {
        it[KEY_PICKY] = if (value) {
            "picky"
        } else {
            "default"
        }
    }

    override fun getLanguage(): Flow<LanguageStoreModel?> =
        data.map { it[KEY_LANGUAGE] }
            .map {
                it?.let {
                    json.decodeFromString<LanguageStoreModel?>(it)
                }
            }
            .distinctUntilChanged()

    override suspend fun setLanguage(value: LanguageStoreModel?) = edit {
        value?.let { value ->
            it[KEY_LANGUAGE] = Json.encodeToString(value)
        } ?: run {
            it.remove(KEY_LANGUAGE)
        }
    }

    override fun getUsername(): Flow<String?> =
        data.map { it[KEY_USERNAME] }
            .map { it?.takeIfNotBlank() }
            .distinctUntilChanged()

    override suspend fun setUsername(value: String) = edit {
        it[KEY_USERNAME] = value
    }

    override fun getApiKey(): Flow<String?> =
        data.map { it[KEY_API_KEY] }
            .map { it?.takeIfNotBlank() }
            .distinctUntilChanged()

    override suspend fun setApiKey(value: String) = edit {
        it[KEY_API_KEY] = value
    }

    override fun getApiUrl(): Flow<String?> =
        data.map { it[KEY_API_URL] }
            .map { it?.takeIfNotBlank() }
            .distinctUntilChanged()

    override suspend fun setApiUrl(value: String) = edit {
        it[KEY_API_URL] = value
    }

    companion object {
        private const val STORE_NAME = "app_preferences"
        private val Context.store by preferencesDataStore(STORE_NAME)

        private val KEY_PICKY = stringPreferencesKey("picky")
        private val KEY_LANGUAGE = stringPreferencesKey("language")
        private val KEY_API_URL = stringPreferencesKey("api_url")
        private val KEY_API_KEY = stringPreferencesKey("api_key")
        private val KEY_USERNAME = stringPreferencesKey("api_key")
    }
}

internal fun AppPreferences.getCorrectionConfig(): Flow<CorrectionConfig> =
    combine(getPicky(), getLanguage()) { picky, language ->
        CorrectionConfig(
            language = language?.toDomain()?.longCode,
            isPicky = picky,
            motherTongue = null,
        )
    }.distinctUntilChanged()

internal fun AppPreferences.getApiCredentials(): Flow<ApiCredentials?> =
    combine(getUsername(), getApiKey()) { userName, apiKey ->
        nullable {
            ApiCredentials(
                userName = userName.bind(),
                apiKey = apiKey.bind(),
            )
        }
    }.distinctUntilChanged()

