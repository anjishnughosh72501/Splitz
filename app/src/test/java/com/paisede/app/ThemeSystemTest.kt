package com.paisede.app

import com.paisede.app.data.preferences.SplitzVisualTheme
import com.paisede.app.ui.theme.ThemePalettes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ThemeSystemTest {

    @Test
    fun `visual theme fromId returns expected enum and handles invalid fallback safely`() {
        assertEquals(SplitzVisualTheme.KISSATEN, SplitzVisualTheme.fromId("KISSATEN"))
        assertEquals(SplitzVisualTheme.KISSATEN, SplitzVisualTheme.fromId("kissaten"))
        assertEquals(SplitzVisualTheme.GRAPHITE, SplitzVisualTheme.fromId("GRAPHITE"))
        assertEquals(SplitzVisualTheme.GRAPHITE, SplitzVisualTheme.fromId("graphite"))
        assertEquals(SplitzVisualTheme.ELECTRIC_BLUE, SplitzVisualTheme.fromId("ELECTRIC_BLUE"))
        assertEquals(SplitzVisualTheme.ELECTRIC_BLUE, SplitzVisualTheme.fromId("electric_blue"))
        assertEquals(SplitzVisualTheme.ULTRAVIOLET, SplitzVisualTheme.fromId("ULTRAVIOLET"))
        assertEquals(SplitzVisualTheme.ULTRAVIOLET, SplitzVisualTheme.fromId("ultraviolet"))
        assertEquals(SplitzVisualTheme.EMERALD, SplitzVisualTheme.fromId("EMERALD"))
        assertEquals(SplitzVisualTheme.EMERALD, SplitzVisualTheme.fromId("emerald"))

        // Fallback for null or unknown values (flagship default is KISSATEN)
        assertEquals(SplitzVisualTheme.KISSATEN, SplitzVisualTheme.fromId(null))
        assertEquals(SplitzVisualTheme.KISSATEN, SplitzVisualTheme.fromId("UNKNOWN_THEME"))
        assertEquals(SplitzVisualTheme.KISSATEN, SplitzVisualTheme.fromId(""))
    }

    @Test
    fun `theme palettes provide complete Light and Dark modes of basic Splitz theme`() {
        val lightPalette = ThemePalettes.forMode(false)
        val darkPalette = ThemePalettes.forMode(true)

        assertEquals(ThemePalettes.Light, lightPalette)
        assertEquals(ThemePalettes.Dark, darkPalette)

        assertNotNull(lightPalette)
        assertNotNull(darkPalette)

        // Light & Dark backgrounds differ
        assertNotEquals(lightPalette.background, darkPalette.background)
        assertNotEquals(lightPalette.textPrimary, darkPalette.textPrimary)

        // Financial status indicators are active and properly defined for both modes
        assertNotNull(lightPalette.positive)
        assertNotNull(darkPalette.positive)
        assertNotNull(lightPalette.negative)
        assertNotNull(darkPalette.negative)
    }
}
