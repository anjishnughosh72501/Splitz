package com.paisede.app.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

object SplitzMotion {
    object Duration {
        const val Instant = 50
        const val Fast = 150
        const val Normal = 250
        const val Slow = 350
        const val Slower = 500
        const val PageEnter = 400
        const val StaggerItem = 80
        const val NumberChange = 600
        const val Success = 400
    }

    object Easing {
        val Standard: androidx.compose.animation.core.Easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
        val Emphasized: androidx.compose.animation.core.Easing = CubicBezierEasing(0.05f, 0.0f, 0.1f, 1.0f)
        val Decelerated: androidx.compose.animation.core.Easing = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)
        val Accelerated: androidx.compose.animation.core.Easing = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)

        fun <T> springDefault(): SpringSpec<T> = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        fun <T> springGentle(): SpringSpec<T> = spring(dampingRatio = Spring.DampingRatioHighBouncy)
        fun <T> springStiff(): SpringSpec<T> = spring(dampingRatio = Spring.DampingRatioLowBouncy)
    }

    fun <T> fastTween() = tween<T>(Duration.Fast, easing = Easing.Standard)
    fun <T> normalTween() = tween<T>(Duration.Normal, easing = Easing.Standard)
    fun <T> slowTween() = tween<T>(Duration.Slow, easing = Easing.Standard)
    fun <T> emphasizedTween() = tween<T>(Duration.Normal, easing = Easing.Emphasized)
    fun <T> deceleratedTween() = tween<T>(Duration.Slow, easing = Easing.Decelerated)
    fun <T> pageEnterTween() = tween<T>(Duration.PageEnter, easing = Easing.Emphasized)
    fun <T> staggerTween() = tween<T>(Duration.Fast, easing = Easing.Decelerated)
    fun <T> numberChangeTween() = tween<T>(Duration.NumberChange, easing = Easing.Decelerated)
    fun <T> successSpring() = spring<T>(dampingRatio = Spring.DampingRatioMediumBouncy)
}

object SplitzTransitionPatterns {
    val FadeIn = "fadeIn"
    val FadeOut = "fadeOut"
    val SlideUp = "slideUp"
    val SlideDown = "slideDown"
    val ScaleIn = "scaleIn"
    val ScaleOut = "scaleOut"
    val Expand = "expand"
    val Collapse = "collapse"
    val Crossfade = "crossfade"
    val SharedExpand = "sharedExpand"
    val Stagger = "stagger"
    val NumberChange = "numberChange"
    val Selection = "selection"
    val Success = "success"
}