package com.paisede.app.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.Slate600
import com.paisede.app.util.CurrencyUtils

@Composable
fun MoneyText(
    amountPaise: Long,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.titleMedium,
    fontWeight: FontWeight? = null,
    showSign: Boolean = false,
    colorMode: MoneyColorMode = MoneyColorMode.AUTO,
    alwaysShowDecimals: Boolean = false
) {
    val textColor = when (colorMode) {
        MoneyColorMode.AUTO -> when {
            amountPaise > 0L -> CreditGreen
            amountPaise < 0L -> DebtRed
            else -> Slate600
        }
        MoneyColorMode.CREDIT -> CreditGreen
        MoneyColorMode.DEBT -> DebtRed
        MoneyColorMode.NEUTRAL -> MaterialTheme.colorScheme.onSurface
    }

    Text(
        text = CurrencyUtils.formatPaise(
            paise = amountPaise,
            showSign = showSign,
            alwaysShowDecimals = alwaysShowDecimals
        ),
        modifier = modifier,
        style = style,
        fontWeight = fontWeight ?: style.fontWeight,
        color = textColor
    )
}

enum class MoneyColorMode {
    AUTO,
    CREDIT,
    DEBT,
    NEUTRAL
}
