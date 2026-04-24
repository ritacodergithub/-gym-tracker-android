package com.example.e_commerce.ui.screens.history

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_commerce.domain.model.Workout
import com.example.e_commerce.ui.AppViewModelProvider
import com.example.e_commerce.ui.components.GlassCard
import com.example.e_commerce.ui.theme.Gradients
import com.example.e_commerce.ui.theme.NeonLime
import com.example.e_commerce.ui.theme.NeonPink
import com.example.e_commerce.ui.theme.TextSecondary
import java.text.DateFormat
import java.util.Date

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val workouts by viewModel.workouts.collectAsStateWithLifecycle()

    if (workouts.isEmpty()) {
        EmptyState()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "History",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "${workouts.size} workouts logged",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary
                )
                Spacer(Modifier.height(8.dp))
            }
            items(workouts, key = { it.id }) { workout ->
                WorkoutGlass(workout = workout, onDelete = { viewModel.delete(workout) })
            }
            item { Spacer(Modifier.height(96.dp)) }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Gradients.Primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = androidx.compose.ui.graphics.Color.White
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "No workouts yet",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "Tap Log workout to record your first session.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun WorkoutGlass(workout: Workout, onDelete: () -> Unit) {
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
                        workout.exerciseName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "${workout.category.displayName} · ${formatDate(workout.performedAtMillis)}",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete workout",
                        tint = NeonPink
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            workout.sets.forEach { set ->
                Text(
                    "Set ${set.setNumber}: ${set.reps} reps × ${formatWeight(set.weightKg)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            if (workout.notes.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    "“${workout.notes}”",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Volume",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary
                )
                Spacer(Modifier.size(6.dp))
                Text(
                    formatWeight(workout.totalVolume),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = NeonLime
                )
            }
        }
    }
}

private fun formatDate(millis: Long): String =
    DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(millis))

private fun formatWeight(kg: Float): String =
    if (kg % 1f == 0f) "${kg.toInt()} kg" else "%.1f kg".format(kg)
