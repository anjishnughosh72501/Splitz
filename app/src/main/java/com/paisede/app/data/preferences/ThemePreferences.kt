package com.paisede.app.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppThemeMode(val displayName: String) {
    SYSTEM("System Default"),
    LIGHT("Light Theme"),
    DARK("Dark Theme")
}

enum class AppFontSize(val displayName: String, val scale: Float) {
    SMALL("Small", 0.85f),
    MEDIUM("Medium", 1.0f),
    LARGE("Large", 1.15f)
}

class ThemePreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("splitz_theme_prefs", Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(loadThemeMode())
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    private val _fontSize = MutableStateFlow(loadFontSize())
    val fontSize: StateFlow<AppFontSize> = _fontSize.asStateFlow()

    private fun loadThemeMode(): AppThemeMode {
        val saved = prefs.getString("theme_mode", AppThemeMode.SYSTEM.name)
        return try {
            AppThemeMode.valueOf(saved ?: AppThemeMode.SYSTEM.name)
        } catch (e: Exception) {
            AppThemeMode.SYSTEM
        }
    }

    fun setThemeMode(mode: AppThemeMode) {
        prefs.edit().putString("theme_mode", mode.name).apply()
        _themeMode.value = mode
    }

    private fun loadFontSize(): AppFontSize {
        val saved = prefs.getString("font_size", AppFontSize.MEDIUM.name)
        return try {
            AppFontSize.valueOf(saved ?: AppFontSize.MEDIUM.name)
        } catch (e: Exception) {
            AppFontSize.MEDIUM
        }
    }

    fun setFontSize(size: AppFontSize) {
        prefs.edit().putString("font_size", size.name).apply()
        _fontSize.value = size
    }
}
