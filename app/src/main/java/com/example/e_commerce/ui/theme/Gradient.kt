package com.example.e_commerce.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Named gradient brushes used across modern components. Centralized so the
// brand stays consistent even as screens evolve.
object Gradients {

    val Primary: Brush = Brush.linearGradient(
        colors = listOf(NeonIndigo, NeonPurple, NeonPink)
    )

    val Cool: Brush = Brush.linearGradient(
        colors = listOf(NeonCyan, NeonIndigo)
    )

    val Fire: Brush = Brush.linearGradient(
        colors = listOf(NeonAmber, NeonRose)
    )

    val Lime: Brush = Brush.linearGradient(
        colors = listOf(Color(0xFFCAFF7A), NeonLime, NeonCyan)
    )

    // Subtle frame around glass cards — creates the "lit edge" look.
    val GlassBorder: Brush = Brush.linearGradient(
        colors = listOf(Color(0x55FFFFFF), Color(0x10FFFFFF), Color(0x22FFFFFF))
    )

    // Radial backdrop used on the top of scroll-heavy screens for depth.
    fun pageBackdrop(): Brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF121632),
            0.35f to Color(0xFF0A0D22),
            1.0f to BgDeep
        )
    )

    // Soft translucent fill for glass cards (no true blur to stay compatible
    // with pre-Android 12 devices — light translucent gradient looks close).
    val GlassFill: Brush = Brush.verticalGradient(
        colors = listOf(Color(0x22FFFFFF), Color(0x08FFFFFF))
    )
}