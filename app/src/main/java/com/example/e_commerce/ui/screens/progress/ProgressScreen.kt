package com.example.e_commerce.ui.screens.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.example.e_commerce.ui.AppViewModelProvider
import com.example.e_commerce.ui.components.GlassCard
import com.example.e_commerce.ui.theme.Gradients
import com.example.e_commerce.ui.theme.NeonCyan
import com.example.e_commerce.ui.theme.NeonLime
import com.example.e_commerce.ui.theme.StrokeSubtle
import com.example.e_commerce.ui.theme.TextSecondary

@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Progress",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Last 7 days",
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                StatGlass(
                    label = "WORKOUTS",
                    value = state.totalWorkouts.toString(),
                    accentColor = NeonCyan,
                    modifier = Modifier.weight(1f)
                )
                StatGlass(
                    label = "VOLUME",
                    value = formatVolume(state.totalVolume),
                    accentColor = NeonLime,
                    unit = "kg lifted",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        "Daily volume",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(12.dp))
                    WeeklyBarChart(bars = state.last7Days, barBrush = Gradients.Primary)
                }
            }
        }

        item { Spacer(Modifier.height(96.dp)) }
    }
}

@Composable
private fun StatGlass(
    label: String,
    value: String,
    accentColor: androidx.compose.ui.graphics.Color,
    unit: String = "",
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier) {
        Column(Modifier.padding(18.dp)) {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = accentColor,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                value,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (unit.isNotBlank()) {
                Text(
                    unit,
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun WeeklyBarChart(
    bars: List<ProgressViewModel.DailyVolume>,
    barBrush: Brush
) {
    val maxVolume = (bars.maxOfOrNull { it.volume } ?: 0f).coerceAtLeast(1f)
    val textStyle = MaterialTheme.typography.labelLarge
    val labelColor = MaterialTheme.colorScheme.onSurface
    val textMeasurer = androidx.compose.ui.text.rememberTextMeasurer()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (bars.isEmpty()) return@Canvas
            val chartHeight = size.height - 36f
            val slotWidth = size.width / bars.size
            val barWidth = slotWidth * 0.6f

            bars.forEachIndexed { i, bar ->
                val barHeight = (bar.volume / maxVolume) * chartHeight
                val left = slotWidth * i + (slotWidth - barWidth) / 2
                val top = chartHeight - barHeight

                drawRoundRect(
                    brush = barBrush,
                    topLeft = Offset(left, top),
                    size = Size(barWidth, barHeight.coerceAtLeast(4f)),
                    cornerRadius = CornerRadius(10f, 10f),
                    style = if (bar.volume == 0f) Stroke(width = 2f) else Fill
                )

                val measured = textMeasurer.measure(
                    text = androidx.compose.ui.text.AnnotatedString(bar.dayLabel),
                    style = textStyle.copy(color = labelColor)
                )
                drawText(
                    textLayoutResult = measured,
                    topLeft = Offset(
                        x = slotWidth * i + (slotWidth - measured.size.width) / 2,
                        y = chartHeight + 10f
                    )
                )
            }

            // Axis line.
            drawLine(
                color = StrokeSubtle,
                start = Offset(0f, chartHeight),
                end = Offset(size.width, chartHeight),
                strokeWidth = 1f
            )
        }
    }
}

private fun formatVolume(kg: Float): String =
    if (kg < 1000) "${kg.toInt()}" else "%.1fk".format(kg / 1000f)
