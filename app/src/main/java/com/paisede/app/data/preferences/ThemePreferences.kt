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

enum class SplitzVisualTheme(
    val id: String,
    val displayName: String,
    val description: String,
    val accentHex: Long
) {
    KISSATEN(
        id = "KISSATEN",
        displayName = "Espresso",
        description = "Warm tactile washi paper, deep espresso, and caramel accents",
        accentHex = 0xFFC87D43
    ),
    GRAPHITE(
        id = "GRAPHITE",
        displayName = "Graphite",
        description = "Minimal, cinematic, refined monochrome with graphite brushed textures",
        accentHex = 0xFF94A3B8
    ),
    ELECTRIC_BLUE(
        id = "ELECTRIC_BLUE",
        displayName = "Ocean",
        description = "Technical, energetic, premium navy & electric cyan-blue treatments",
        accentHex = 0xFF3B82F6
    ),
    ULTRAVIOLET(
        id = "ULTRAVIOLET",
        displayName = "Amethyst",
        description = "Expressive, atmospheric, bold violet & deep twilight contours",
        accentHex = 0xFF8B5CF6
    ),
    EMERALD(
        id = "EMERALD",
        displayName = "Emerald",
        description = "Balanced, modern, fresh botanical jade & forest contours",
        accentHex = 0xFF10B981
    );

    companion object {
        fun fromId(id: String?): SplitzVisualTheme {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: KISSATEN
        }
    }
}

enum class AppFontSize(val displayName: String, val description: String, val scale: Float) {
    COMPACT("Compact", "0.85x — Higher density", 0.85f),
    DEFAULT("Standard", "1.00x — Default layout", 1.0f),
    LARGE("Large", "1.15x — High legibility", 1.15f),
    EXTRA_LARGE("Extra Large", "1.28x — Maximum visibility", 1.28f);

    companion object {
        // Backwards-compatible aliases
        val SMALL = COMPACT
        val MEDIUM = DEFAULT

        fun fromString(saved: String?): AppFontSize {
            return when (saved?.uppercase()) {
                "COMPACT", "SMALL" -> COMPACT
                "DEFAULT", "MEDIUM" -> DEFAULT
                "LARGE" -> LARGE
                "EXTRA_LARGE" -> EXTRA_LARGE
                else -> DEFAULT
            }
        }
    }
}

class ThemePreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("splitz_theme_prefs", Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(loadThemeMode())
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    private val _visualTheme = MutableStateFlow(loadVisualTheme())
    val visualTheme: StateFlow<SplitzVisualTheme> = _visualTheme.asStateFlow()

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

    private fun loadVisualTheme(): SplitzVisualTheme {
        val saved = prefs.getString("visual_theme", SplitzVisualTheme.KISSATEN.name)
        return SplitzVisualTheme.fromId(saved)
    }

    fun setVisualTheme(theme: SplitzVisualTheme) {
        prefs.edit().putString("visual_theme", theme.name).apply()
        _visualTheme.value = theme
    }

    private fun loadFontSize(): AppFontSize {
        val saved = prefs.getString("font_size", AppFontSize.DEFAULT.name)
        return AppFontSize.fromString(saved)
    }

    fun setFontSize(size: AppFontSize) {
        prefs.edit().putString("font_size", size.name).apply()
        _fontSize.value = size
    }
}
