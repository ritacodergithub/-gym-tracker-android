package com.example.e_commerce.ui.screens.routines

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_commerce.data.catalog.ExerciseLibrary
import com.example.e_commerce.data.catalog.Routine
import com.example.e_commerce.data.catalog.RoutineDay
import com.example.e_commerce.ui.AppViewModelProvider
import com.example.e_commerce.ui.components.GlassCard
import com.example.e_commerce.ui.theme.Gradients
import com.example.e_commerce.ui.theme.NeonCyan
import com.example.e_commerce.ui.theme.NeonLime
import com.example.e_commerce.ui.theme.TextSecondary

@Composable
fun RoutinesScreen(
    viewModel: RoutinesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Routines",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Proven programs. Pick one and follow the days.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
            Spacer(Modifier.height(4.dp))
        }
        items(items = viewModel.routines, key = { it.id }) { routine ->
            RoutineGlass(routine)
        }
        item { Spacer(Modifier.height(96.dp)) }
    }
}

@Composable
private fun RoutineGlass(routine: Routine) {
    var expanded by remember { mutableStateOf(false) }

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Gradients.Primary)
                )
                Spacer(Modifier.size(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        routine.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        routine.summary,
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary
                    )
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = NeonCyan
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                AssistChip(
                    onClick = {},
                    label = { Text("${routine.daysPerWeek}×/wk") },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = NeonCyan.copy(alpha = 0.2f),
                        labelColor = NeonCyan
                    )
                )
                AssistChip(
                    onClick = {},
                    label = { Text(routine.level.label) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = NeonLime.copy(alpha = 0.2f),
                        labelColor = NeonLime
                    )
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(Modifier.padding(top = 12.dp)) {
                    routine.days.forEachIndexed { idx, day ->
                        RoutineDayBlock(day)
                        if (idx != routine.days.lastIndex) Spacer(Modifier.height(14.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun RoutineDayBlock(day: RoutineDay) {
    Column {
        Text(
            day.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = NeonCyan
        )
        Spacer(Modifier.height(4.dp))
        day.exercises.forEach { re ->
            val exercise = ExerciseLibrary.find(re.exerciseId)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
            ) {
                Text(
                    exercise?.name ?: re.exerciseId,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "${re.sets} × ${re.reps}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = NeonLime,
                    fontWeight = FontWeight.SemiBold
                )
            }
            if (re.notes.isNotBlank()) {
                Text(
                    re.notes,
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary
                )
            }
        }
    }
}