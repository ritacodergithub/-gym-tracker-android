package com.example.e_commerce.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.e_commerce.ui.theme.Gradients

// Frosted-glass-style card. Semi-translucent gradient fill + 1dp gradient
// border gives a modern "lit edge" look without needing RenderEffect blur
// (which would break on pre-Android 12 devices).
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(22.dp),
    fill: Brush = Gradients.GlassFill,
    borderBrush: Brush = Gradients.GlassBorder,
    content: @Composable ColumnScope.() -> Unit
) {
    val base = modifier
        .clip(shape)
        .background(brush = fill, shape = shape)
        .border(BorderStroke(1.dp, borderBrush), shape)

    val wired = if (onClick != null) base.clickable(onClick = onClick) else base

    Box(modifier = wired) {
        Column(content = content)
    }
}