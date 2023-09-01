package cz.lastaapps.languagetool.ui.features.settings

import arrow.core.Either
import cz.lastaapps.languagetool.core.StateViewModel
import cz.lastaapps.languagetool.core.VMState
import cz.lastaapps.languagetool.core.launchVM
import cz.lastaapps.languagetool.data.AppPreferences
import cz.lastaapps.languagetool.data.LangToolRepository
import cz.lastaapps.languagetool.ui.features.settings.model.ApiUrlState
import cz.lastaapps.languagetool.ui.features.settings.model.CredentialsState
import kotlinx.coroutines.flow.first

internal class SettingsViewModel(
    private val preferences: AppPreferences,
    private val repo: LangToolRepository,
) : StateViewModel<SettingsState>(SettingsState()) {

    fun onAppear() = launchOnlyOnce {
        preferences.getApiUrl().first()
            ?.let {
                updateState {
                    copy(apiUrl = apiUrl.copy(url = it))
                }
            }
        preferences.getUsername().first()
            ?.let {
                updateState {
                    copy(credentials = credentials.copy(username = it))
                }
            }
        preferences.getApiKey().first()
            ?.let {
                updateState {
                    copy(credentials = credentials.copy(apiKey = it))
                }
            }
    }

    fun saveApiUrl(apiUrl: String) = launchVM {
        var url = apiUrl.trim()
        if (url.isNotEmpty()) {
            if (!(url.startsWith("https://") || url.startsWith("http://"))) {
                url = "https://$url"
            }
            if (url.endsWith("/")) {
                url = url.dropLast(1)
            }
            if (url.endsWith("/v2")) {
                url = url.dropLast(3)
            }
        }

        preferences.setApiUrl(url)

        updateState {
            copy(
                apiUrl = this.apiUrl.copy(
                    url = url,
                    isChecking = true,
                ),
            )
        }

        when (repo.correctText("This is wong.")) {
            is Either.Right -> {
                updateState {
                    copy(
                        apiUrl = this.apiUrl.copy(
                            isChecking = false,
                            isValid = true,
                        ),
                    )
                }
            }

            is Either.Left -> {
                updateState {
                    copy(
                        apiUrl = this.apiUrl.copy(
                            isChecking = false,
                            isValid = false,
                        ),
                    )
                }
            }
        }
    }

    fun saveCredentials(username: String, apiKey: String) = launchVM {
        preferences.setUsername(username.trim())
        preferences.setApiKey(apiKey.trim())

        updateState {
            copy(
                credentials = credentials.copy(
                    username = username,
                    apiKey = apiKey,
                    isChecking = true,
                ),
            )
        }

        when (repo.correctText("This is wong.")) {
            is Either.Right -> {
                updateState {
                    copy(
                        credentials = credentials.copy(
                            isChecking = false,
                            isValid = username.isBlank() == apiKey.isBlank(),
                        ),
                    )
                }
            }

            is Either.Left -> {
                updateState {
                    copy(
                        credentials = credentials.copy(
                            isChecking = false,
                            isValid = false,
                        ),
                    )
                }
            }
        }
    }
}

internal data class SettingsState(
    val apiUrl: ApiUrlState = ApiUrlState(
        "",
        isChecking = false,
        isValid = null,
    ),
    val credentials: CredentialsState = CredentialsState(
        "",
        "",
        isChecking = false,
        isValid = null,
    ),
) : VMState
