package com.example.e_commerce.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.e_commerce.ui.theme.Gradients

// Extended FAB with a softly pulsing glow behind it. Uses Modifier.blur
// (available on Android 12+, gracefully no-ops on older devices) for the
// "energy field" look without needing drawBehind shader gymnastics.
@Composable
fun GlowFab(
    onClick: () -> Unit,
    label: String = "Log workout",
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "fabGlow")
    val alpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1_600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fabAlpha"
    )

    Box(modifier = modifier) {
        // Glow halo
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(24.dp)
                .alpha(alpha)
                .clip(RoundedCornerShape(28.dp))
                .background(Gradients.Primary)
        )
        // Actual pill
        Row(
            modifier = Modifier
                .height(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Gradients.Primary)
                .clickable(onClick = onClick)
                .padding(horizontal = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                label,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }
    }
}