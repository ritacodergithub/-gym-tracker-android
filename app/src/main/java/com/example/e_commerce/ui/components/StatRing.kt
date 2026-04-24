package com.example.e_commerce.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.e_commerce.ui.theme.Gradients
import com.example.e_commerce.ui.theme.StrokeSubtle

// Animated circular progress ring. The arc sweep tween is done by
// animateFloatAsState so the UI smoothly catches up when the backing
// StateFlow updates (e.g. streak goes from 3 → 4).
@Composable
fun StatRing(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 140.dp,
    strokeWidth: Dp = 10.dp,
    gradient: Brush = Gradients.Primary,
    trackColor: Color = StrokeSubtle,
    content: @Composable BoxScope.() -> Unit
) {
    val clamped = progress.coerceIn(0f, 1f)
    val animated by animateFloatAsState(targetValue = clamped, label = "ring")

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = strokeWidth.toPx()
            val diameter = this.size.minDimension - stroke
            val offset = Offset(stroke / 2f, stroke / 2f)
            val arcSize = Size(diameter, diameter)

            drawCircle(
                color = trackColor,
                radius = diameter / 2f,
                style = Stroke(width = stroke)
            )
            drawArc(
                brush = gradient,
                startAngle = -90f,
                sweepAngle = 360f * animated,
                useCenter = false,
                topLeft = offset,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
        content()
    }
}