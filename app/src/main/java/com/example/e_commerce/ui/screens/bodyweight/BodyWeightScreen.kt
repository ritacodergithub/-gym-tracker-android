package com.example.e_commerce.ui.screens.bodyweight

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_commerce.data.local.BodyWeightEntity
import com.example.e_commerce.data.local.WeightUnit
import com.example.e_commerce.ui.AppViewModelProvider
import com.example.e_commerce.ui.components.GlassCard
import com.example.e_commerce.ui.components.GradientButton
import com.example.e_commerce.ui.components.PageBackground
import com.example.e_commerce.ui.theme.Gradients
import com.example.e_commerce.ui.theme.NeonCyan
import com.example.e_commerce.ui.theme.NeonLime
import com.example.e_commerce.ui.theme.NeonPink
import com.example.e_commerce.ui.theme.TextSecondary
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyWeightScreen(
    onBack: () -> Unit,
    viewModel: BodyWeightViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val unit = state.profile?.weightUnit ?: WeightUnit.KG

    PageBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Body weight") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { inner ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InputCard(
                    value = state.weightInput,
                    onChange = viewModel::onInputChange,
                    onLog = viewModel::log,
                    error = state.error
                )

                TrendCard(entries = state.entries)

                Text(
                    "History",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 4.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items = state.entries, key = { it.id }) { entry ->
                        HistoryRow(
                            entry = entry,
                            unit = unit,
                            onDelete = { viewModel.delete(entry) }
                        )
                    }
                    item { Spacer(Modifier.height(60.dp)) }
                }
            }
        }
    }
}

@Composable
private fun InputCard(
    value: String,
    onChange: (String) -> Unit,
    onLog: () -> Unit,
    error: String?
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                "Today's weigh-in",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = value,
                onValueChange = onChange,
                label = { Text("Weight (kg)") },
                singleLine = true,
                isError = error != null,
                supportingText = { error?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0x14FFFFFF),
                    unfocusedContainerColor = Color(0x0DFFFFFF),
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = Color(0x33FFFFFF),
                    focusedLabelColor = NeonCyan,
                    cursorColor = NeonLime
                ),
                modifier = Modifier.fillMaxWidth()
            )
            GradientButton(
                text = "Log weight",
                onClick = onLog,
                gradient = Gradients.Lime,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TrendCard(entries: List<BodyWeightEntity>) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "Trend",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(12.dp))
            if (entries.size < 2) {
                Text(
                    "Log at least two weigh-ins to see a trend.",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary
                )
            } else {
                WeightLine(entries = entries.sortedBy { it.measuredAt }, lineColor = NeonCyan)
            }
        }
    }
}

@Composable
private fun HistoryRow(entry: BodyWeightEntity, unit: WeightUnit, onDelete: () -> Unit) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    unit.format(entry.weightKg),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                        .format(Date(entry.measuredAt)),
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete entry", tint = NeonPink)
            }
        }
    }
}

@Composable
private fun WeightLine(entries: List<BodyWeightEntity>, lineColor: Color) {
    val min = entries.minOf { it.weightKg }
    val max = entries.maxOf { it.weightKg }
    val range = (max - min).coerceAtLeast(1f)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        val stepX = size.width / (entries.size - 1)
        val path = Path()
        entries.forEachIndexed { i, e ->
            val x = stepX * i
            val normalized = (e.weightKg - min) / range
            val y = size.height - (normalized * size.height)
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path = path, color = lineColor, style = Stroke(width = 6f))
        entries.forEachIndexed { i, e ->
            val x = stepX * i
            val normalized = (e.weightKg - min) / range
            val y = size.height - (normalized * size.height)
            drawCircle(color = lineColor, radius = 8f, center = Offset(x, y))
            drawCircle(color = Color.White, radius = 3f, center = Offset(x, y))
        }
    }
}