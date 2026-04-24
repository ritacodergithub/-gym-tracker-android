package com.example.e_commerce.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.e_commerce.data.catalog.Exercise
import com.example.e_commerce.data.catalog.FreeExerciseDb
import com.example.e_commerce.ui.theme.Gradients
import com.example.e_commerce.ui.theme.NeonCyan
import com.example.e_commerce.ui.theme.TextSecondary
import kotlinx.coroutines.delay

// Shows an in-app animated exercise demonstration by cross-fading two
// JPG frames from yuhonas/free-exercise-db. Always offers a button to
// watch the full tutorial on YouTube. Works offline after Coil has
// cached the frames once.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisePreviewSheet(
    exercise: Exercise,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val frames = remember(exercise.id) { FreeExerciseDb.previewFramesFor(exercise.id) }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PlayCircle, contentDescription = null, tint = NeonCyan)
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        exercise.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        if (frames != null) "Animated preview" else "Preview unavailable",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary
                    )
                }
            }

            if (frames != null) {
                AnimatedPreview(frames)
            } else {
                // No slug mapping — show a hint strip where the preview would be.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x14FFFFFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No offline preview for this exercise yet.\nTap below to watch a free tutorial on YouTube.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )
                }
            }

            // Primary CTA — still free, just a different medium.
            GradientButton(
                text = "Watch full tutorial on YouTube",
                icon = Icons.Default.OpenInNew,
                onClick = {
                    launchExerciseVideo(context, exercise)
                    onDismiss()
                },
                gradient = Gradients.Primary,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

// Two AsyncImages stacked; a boolean flips every ~700ms and Crossfade
// animates the swap. If either image fails to load (404, no network),
// the user still sees whatever loaded + the YouTube button.
@Composable
private fun AnimatedPreview(frames: FreeExerciseDb.PreviewFrames) {
    var flip by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Pre-cache frame 1 on first composition so the crossfade is snappy.
    LaunchedEffect(frames) {
        coil.Coil.imageLoader(context).enqueue(
            ImageRequest.Builder(context).data(frames.frame1).build()
        )
        // Flip once both frames are reasonably likely to be warm.
        while (true) {
            delay(700)
            flip = !flip
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
    ) {
        Crossfade(
            targetState = flip,
            animationSpec = tween(durationMillis = 400),
            label = "exercise-frame",
            modifier = Modifier.fillMaxSize()
        ) { isSecond ->
            AsyncImage(
                model = if (isSecond) frames.frame1 else frames.frame0,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}