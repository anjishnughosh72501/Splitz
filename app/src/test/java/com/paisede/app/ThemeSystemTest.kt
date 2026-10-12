package com.paisede.app

import com.paisede.app.data.preferences.SplitzVisualTheme
import com.paisede.app.ui.theme.ThemePalettes
import org.junit.Assert.assertEquals
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
    fun `theme palettes provide correct asset drawables and distinct colors`() {
        val kissatenPalette = ThemePalettes.forTheme(SplitzVisualTheme.KISSATEN)
        val graphitePalette = ThemePalettes.forTheme(SplitzVisualTheme.GRAPHITE)
        val bluePalette = ThemePalettes.forTheme(SplitzVisualTheme.ELECTRIC_BLUE)
        val violetPalette = ThemePalettes.forTheme(SplitzVisualTheme.ULTRAVIOLET)
        val emeraldPalette = ThemePalettes.forTheme(SplitzVisualTheme.EMERALD)

        assertNotNull(kissatenPalette)
        assertNotNull(emeraldPalette)
        assertEquals(R.drawable.bg_graphite_texture, graphitePalette.textureResId)
        assertEquals(R.drawable.bg_electric_blue_texture, bluePalette.textureResId)
        assertEquals(R.drawable.bg_ultraviolet_texture, violetPalette.textureResId)

        // Financial status colors remain consistent across themes
        assertEquals(graphitePalette.positive, bluePalette.positive)
        assertEquals(bluePalette.positive, violetPalette.positive)
        assertEquals(graphitePalette.negative, bluePalette.negative)
        assertEquals(bluePalette.negative, violetPalette.negative)

        // Brand colors differ distinctly per theme
        assertTrue(kissatenPalette.brandPrimary != graphitePalette.brandPrimary)
        assertTrue(graphitePalette.brandPrimary != bluePalette.brandPrimary)
        assertTrue(bluePalette.brandPrimary != violetPalette.brandPrimary)
        assertTrue(violetPalette.brandPrimary != emeraldPalette.brandPrimary)
    }
}
