package com.paisede.app.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppCurrency(
    val code: String,
    val symbol: String,
    val displayName: String,
    val subunitName: String,
    val isIndianNumbering: Boolean
) {
    INR("INR", "₹", "Indian Rupee (₹ INR)", "Paise", true),
    USD("USD", "$", "US Dollar ($ USD)", "Cents", false),
    EUR("EUR", "€", "Euro (€ EUR)", "Cents", false),
    GBP("GBP", "£", "British Pound (£ GBP)", "Pence", false),
    JPY("JPY", "¥", "Japanese Yen (¥ JPY)", "Sen", false),
    CAD("CAD", "CA$", "Canadian Dollar (CA$ CAD)", "Cents", false),
    AUD("AUD", "A$", "Australian Dollar (A$ AUD)", "Cents", false),
    SGD("SGD", "S$", "Singapore Dollar (S$ SGD)", "Cents", false),
    AED("AED", "د.إ", "UAE Dirham (AED)", "Fils", false);

    companion object {
        val DEFAULT = INR

        fun fromCode(code: String?): AppCurrency {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: DEFAULT
        }
    }
}

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("splitz_user_prefs", Context.MODE_PRIVATE)

    private val _userName = MutableStateFlow(loadUserName())
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _currency = MutableStateFlow(loadCurrency())
    val currency: StateFlow<AppCurrency> = _currency.asStateFlow()

    private val _isStartupCompleted = MutableStateFlow(loadIsStartupCompleted())
    val isStartupCompleted: StateFlow<Boolean> = _isStartupCompleted.asStateFlow()

    private fun loadUserName(): String {
        return prefs.getString(KEY_USER_NAME, "") ?: ""
    }

    private fun loadCurrency(): AppCurrency {
        val code = prefs.getString(KEY_CURRENCY_CODE, AppCurrency.DEFAULT.code)
        return AppCurrency.fromCode(code)
    }

    private fun loadIsStartupCompleted(): Boolean {
        // If user already saved a username before, consider startup complete
        return prefs.getBoolean(KEY_STARTUP_COMPLETED, false) || (loadUserName().isNotBlank())
    }

    fun completeStartup(name: String, currency: AppCurrency) {
        val trimmed = name.trim()
        prefs.edit()
            .putString(KEY_USER_NAME, trimmed)
            .putString(KEY_CURRENCY_CODE, currency.code)
            .putBoolean(KEY_STARTUP_COMPLETED, true)
            .apply()
        _userName.value = trimmed
        _currency.value = currency
        _isStartupCompleted.value = true
    }

    fun setUserName(name: String) {
        val trimmed = name.trim()
        prefs.edit().putString(KEY_USER_NAME, trimmed).apply()
        _userName.value = trimmed
    }

    fun setCurrency(currency: AppCurrency) {
        prefs.edit().putString(KEY_CURRENCY_CODE, currency.code).apply()
        _currency.value = currency
    }

    companion object {
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_CURRENCY_CODE = "currency_code"
        private const val KEY_STARTUP_COMPLETED = "startup_completed"
    }
}
