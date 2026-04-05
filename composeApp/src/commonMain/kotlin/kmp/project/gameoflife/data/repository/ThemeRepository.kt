package kmp.project.gameoflife.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kmp.project.gameoflife.getPlatform
import kmp.project.gameoflife.ui.theme.ColorTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ThemeRepository(private val dataStore: DataStore<Preferences>) {

    private val themeKey = stringPreferencesKey("color_theme")

    val defaultTheme = if (getPlatform().isDynamicColorSupported) {
        ColorTheme.DYNAMIC
    } else {
        ColorTheme.SYSTEM
    }

    val theme: Flow<ColorTheme> = dataStore.data.map { preferences ->
        val themeName = preferences[themeKey] ?: defaultTheme.name
        try {
            ColorTheme.valueOf(themeName)
        } catch (_: Exception) {
            defaultTheme
        }
    }

    suspend fun setTheme(theme: ColorTheme) {
        dataStore.edit { preferences ->
            preferences[themeKey] = theme.name
        }
    }

    suspend fun getValue(key: String): String {
        val prefKey = stringPreferencesKey(key)
        return dataStore.data.map { preferences ->
            // Use explicit get(Key) to avoid conflict with delegate getValue or Map.get
            preferences.get(prefKey) ?: "Error"
        }.first()
    }
}
