package com.paisede.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzMotion
import com.paisede.app.ui.theme.SplitzTypography
import com.paisede.app.util.CurrencyUtils

@Composable
fun MoneyText(
    amountPaise: Long,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.titleMedium,
    fontWeight: FontWeight? = null,
    showSign: Boolean = false,
    colorMode: MoneyColorMode = MoneyColorMode.AUTO,
    color: Color? = null,
    alwaysShowDecimals: Boolean = false,
    animate: Boolean = false
) {
    val colors = SplitzColors
    val targetTextColor = color ?: when (colorMode) {
        MoneyColorMode.AUTO -> when {
            amountPaise > 0L -> colors.positive()
            amountPaise < 0L -> colors.negative()
            else -> colors.textMuted()
        }
        MoneyColorMode.CREDIT -> colors.positive()
        MoneyColorMode.DEBT -> colors.negative()
        MoneyColorMode.NEUTRAL -> colors.textPrimary()
        MoneyColorMode.SETTLED -> colors.textMuted()
    }

    val animatedColor by animateColorAsState(
        targetValue = targetTextColor,
        animationSpec = tween(SplitzMotion.Duration.Fast, easing = SplitzMotion.Easing.Standard),
        label = "MoneyColorAnimation"
    )

    val currentCurrency = com.paisede.app.ui.theme.LocalAppCurrency.current

    if (animate) {
        val animatedPaise by animateFloatAsState(
            targetValue = amountPaise.toFloat(),
            animationSpec = tween(SplitzMotion.Duration.NumberChange, easing = SplitzMotion.Easing.Decelerated),
            label = "MoneyAmountAnimation"
        )
        Text(
            text = CurrencyUtils.formatPaise(
                paise = animatedPaise.toLong(),
                showSign = showSign,
                alwaysShowDecimals = alwaysShowDecimals,
                currency = currentCurrency
            ),
            modifier = modifier,
            style = style,
            fontWeight = fontWeight ?: style.fontWeight,
            color = animatedColor
        )
    } else {
        Text(
            text = CurrencyUtils.formatPaise(
                paise = amountPaise,
                showSign = showSign,
                alwaysShowDecimals = alwaysShowDecimals,
                currency = currentCurrency
            ),
            modifier = modifier,
            style = style,
            fontWeight = fontWeight ?: style.fontWeight,
            color = animatedColor
        )
    }
}

enum class MoneyColorMode {
    AUTO,
    CREDIT,
    DEBT,
    NEUTRAL,
    SETTLED
}
