package com.paisede.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.paisede.app.data.preferences.AppThemeMode
import com.paisede.app.ui.navigation.AppNavigation
import com.paisede.app.ui.theme.SplitzTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as PaiseDeApplication

        setContent {
            val themeMode by app.themePreferences.themeMode.collectAsState()
            val fontSize by app.themePreferences.fontSize.collectAsState()
            val currency by app.userPreferences.currency.collectAsState()
            val isDark = when (themeMode) {
                AppThemeMode.SYSTEM -> isSystemInDarkTheme()
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
            }

            SplitzTheme(
                darkTheme = isDark,
                fontScale = fontSize.scale,
                currency = currency
            ) {
                com.paisede.app.ui.components.SplitzAmbientBackground(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation(app = app)
                }
            }
        }
    }
}
