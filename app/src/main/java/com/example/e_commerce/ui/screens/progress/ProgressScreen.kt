package com.example.e_commerce.ui.screens.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_commerce.ui.AppViewModelProvider

@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Your progress", style = MaterialTheme.typography.headlineMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                title = "Workouts",
                value = state.totalWorkouts.toString(),
                suffix = "last 7d",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Volume",
                value = formatVolume(state.totalVolume),
                suffix = "kg lifted",
                modifier = Modifier.weight(1f)
            )
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "Daily volume (kg)",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(12.dp))
                WeeklyBarChart(
                    bars = state.last7Days,
                    barColor = MaterialTheme.colorScheme.primary,
                    labelColor = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    suffix: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelLarge)
            Text(
                value,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                suffix,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

// Hand-rolled bar chart. Avoids an extra dependency for an MVP visualization;
// swap for a library like Vico once you need stacked bars / tooltips / zoom.
@Composable
private fun WeeklyBarChart(
    bars: List<ProgressViewModel.DailyVolume>,
    barColor: Color,
    labelColor: Color
) {
    val maxVolume = (bars.maxOfOrNull { it.volume } ?: 0f).coerceAtLeast(1f)
    val textStyle = MaterialTheme.typography.labelLarge
    val textMeasurer = androidx.compose.ui.text.rememberTextMeasurer()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (bars.isEmpty()) return@Canvas

            val chartHeight = size.height - 32f // reserve space for day labels
            val slotWidth = size.width / bars.size
            val barWidth = slotWidth * 0.55f

            bars.forEachIndexed { i, bar ->
                val barHeight = (bar.volume / maxVolume) * chartHeight
                val left = slotWidth * i + (slotWidth - barWidth) / 2
                val top = chartHeight - barHeight

                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(left, top),
                    size = Size(barWidth, barHeight.coerceAtLeast(2f)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f),
                    style = if (bar.volume == 0f) Stroke(width = 2f) else androidx.compose.ui.graphics.drawscope.Fill
                )

                // Day label beneath the bar.
                val measured = textMeasurer.measure(
                    text = androidx.compose.ui.text.AnnotatedString(bar.dayLabel),
                    style = textStyle.copy(color = labelColor)
                )
                drawText(
                    textLayoutResult = measured,
                    topLeft = Offset(
                        x = slotWidth * i + (slotWidth - measured.size.width) / 2,
                        y = chartHeight + 8f
                    )
                )
            }
        }
    }
}

private fun formatVolume(kg: Float): String =
    if (kg < 1000) "${kg.toInt()}" else "%.1fk".format(kg / 1000f)