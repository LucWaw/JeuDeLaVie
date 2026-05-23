package kmp.project.gameoflife.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kmp.project.gameoflife.getPlatform
import kmp.project.gameoflife.ui.theme.ColorTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ThemeRepository(private val dataStore: DataStore<Preferences>) {

    private val gridRowsKey = intPreferencesKey("grid_rows")
    private val gridColumnsKey = intPreferencesKey("grid_columns")

    fun getGridRows(): Flow<Int> = dataStore.data.map { preferences ->
        preferences[gridRowsKey] ?: 20
    }

    fun getGridColumns(isTablet: Boolean): Flow<Int> = dataStore.data.map { preferences ->
        preferences[gridColumnsKey] ?: if (isTablet || getPlatform().name.startsWith("Java")) 80 else 20
    }

    suspend fun setGridRows(rows: Int) {
        dataStore.edit { preferences ->
            preferences[gridRowsKey] = rows
        }
    }

    suspend fun setGridColumns(columns: Int) {
        dataStore.edit { preferences ->
            preferences[gridColumnsKey] = columns
        }
    }

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
