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
        assertEquals(SplitzVisualTheme.GRAPHITE, SplitzVisualTheme.fromId("GRAPHITE"))
        assertEquals(SplitzVisualTheme.GRAPHITE, SplitzVisualTheme.fromId("graphite"))
        assertEquals(SplitzVisualTheme.ELECTRIC_BLUE, SplitzVisualTheme.fromId("ELECTRIC_BLUE"))
        assertEquals(SplitzVisualTheme.ELECTRIC_BLUE, SplitzVisualTheme.fromId("electric_blue"))
        assertEquals(SplitzVisualTheme.ULTRAVIOLET, SplitzVisualTheme.fromId("ULTRAVIOLET"))
        assertEquals(SplitzVisualTheme.ULTRAVIOLET, SplitzVisualTheme.fromId("ultraviolet"))

        // Fallback for null or unknown values
        assertEquals(SplitzVisualTheme.GRAPHITE, SplitzVisualTheme.fromId(null))
        assertEquals(SplitzVisualTheme.GRAPHITE, SplitzVisualTheme.fromId("UNKNOWN_THEME"))
        assertEquals(SplitzVisualTheme.GRAPHITE, SplitzVisualTheme.fromId(""))
    }

    @Test
    fun `theme palettes provide correct asset drawables and distinct colors`() {
        val graphitePalette = ThemePalettes.forTheme(SplitzVisualTheme.GRAPHITE)
        val bluePalette = ThemePalettes.forTheme(SplitzVisualTheme.ELECTRIC_BLUE)
        val violetPalette = ThemePalettes.forTheme(SplitzVisualTheme.ULTRAVIOLET)

        assertEquals(R.drawable.bg_graphite_texture, graphitePalette.textureResId)
        assertEquals(R.drawable.bg_electric_blue_texture, bluePalette.textureResId)
        assertEquals(R.drawable.bg_ultraviolet_texture, violetPalette.textureResId)

        // Financial status colors remain consistent across all themes
        assertEquals(graphitePalette.positive, bluePalette.positive)
        assertEquals(bluePalette.positive, violetPalette.positive)
        assertEquals(graphitePalette.negative, bluePalette.negative)
        assertEquals(bluePalette.negative, violetPalette.negative)

        // Brand colors differ distinctly per theme
        assertTrue(graphitePalette.brandPrimary != bluePalette.brandPrimary)
        assertTrue(bluePalette.brandPrimary != violetPalette.brandPrimary)
        assertTrue(graphitePalette.brandPrimary != violetPalette.brandPrimary)
    }
}
