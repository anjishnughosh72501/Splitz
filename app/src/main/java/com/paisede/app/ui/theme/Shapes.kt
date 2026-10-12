package com.paisede.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object SplitzShapes {
    val XS = 8.dp
    val SM = 12.dp
    val MD = 16.dp
    val LG = 20.dp
    val XL = 28.dp
    val Pill = 999.dp

    val cardSmall = SM
    val cardMedium = MD
    val cardLarge = LG
    val cardHero = XL
    val button = Pill
    val buttonPill = Pill
    val receiptCard = 24.dp
    val stamp = 12.dp
    val fab = LG
    val sheet = XL
    val avatar = Pill
    val input = SM
    val chip = Pill
}

val Material3Shapes = Shapes(
    extraSmall = RoundedCornerShape(SplitzShapes.XS),
    small = RoundedCornerShape(SplitzShapes.SM),
    medium = RoundedCornerShape(SplitzShapes.MD),
    large = RoundedCornerShape(SplitzShapes.LG),
    extraLarge = RoundedCornerShape(SplitzShapes.XL)
)