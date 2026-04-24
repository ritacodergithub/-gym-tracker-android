package com.example.e_commerce.ui.components

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestTimerSheet(
    initialSeconds: Int = 90,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        RestTimerContent(initialSeconds = initialSeconds)
        Spacer(Modifier.height(16.dp))
    }
}

// The timer itself is pulled out so it's unit-test-friendly and can be
// embedded inline if we ever want it outside a sheet.
@Composable
private fun RestTimerContent(initialSeconds: Int) {
    val context = LocalContext.current
    var targetSeconds by remember { mutableIntStateOf(initialSeconds) }
    var remainingSeconds by remember { mutableIntStateOf(initialSeconds) }
    var running by remember { mutableStateOf(true) }

    // Countdown: tick once per second while running. DisposableEffect isn't
    // needed — cancellation comes free with LaunchedEffect's keys.
    LaunchedEffect(running, targetSeconds) {
        while (running && remainingSeconds > 0) {
            delay(1_000)
            remainingSeconds--
            if (remainingSeconds == 0) vibrate(context)
        }
    }

    // Reset if the user changes the preset mid-set.
    DisposableEffect(targetSeconds) {
        remainingSeconds = targetSeconds
        running = true
        onDispose { }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Rest", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(180.dp)) {
            CircularProgressIndicator(
                progress = {
                    if (targetSeconds == 0) 0f
                    else remainingSeconds.toFloat() / targetSeconds.toFloat()
                },
                modifier = Modifier.size(180.dp),
                strokeWidth = 8.dp,
                color = if (remainingSeconds == 0)
                    MaterialTheme.colorScheme.secondary
                else
                    MaterialTheme.colorScheme.primary,
                trackColor = Color.LightGray.copy(alpha = 0.3f)
            )
            Text(
                text = formatTime(remainingSeconds),
                style = MaterialTheme.typography.displayLarge
            )
        }

        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(60, 90, 120, 180).forEach { preset ->
                AssistChip(
                    onClick = { targetSeconds = preset },
                    label = { Text("${preset}s") },
                    shape = CircleShape
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { remainingSeconds = targetSeconds; running = true },
                modifier = Modifier.weight(1f)
            ) { Text("Reset") }

            Button(
                onClick = { running = !running },
                modifier = Modifier.weight(1f)
            ) { Text(if (running) "Pause" else "Resume") }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%d:%02d".format(m, s)
}

@Suppress("DEPRECATION")
private fun vibrate(context: android.content.Context) {
    val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = context.getSystemService(Vibrator::class.java) as? Vibrator
            ?: (context.getSystemService(android.content.Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)?.defaultVibrator
        manager
    } else {
        context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
    }
    vibrator ?: return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(250)
    }
}