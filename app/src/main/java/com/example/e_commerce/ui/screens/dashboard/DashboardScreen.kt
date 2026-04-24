package com.example.e_commerce.ui.screens.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_commerce.data.local.UserProfileEntity
import com.example.e_commerce.data.local.WeightUnit
import com.example.e_commerce.domain.model.PersonalRecord
import com.example.e_commerce.domain.model.Workout
import com.example.e_commerce.ui.AppViewModelProvider
import com.example.e_commerce.ui.components.GlassCard
import com.example.e_commerce.ui.components.StatRing
import com.example.e_commerce.ui.theme.Gradients
import com.example.e_commerce.ui.theme.NeonAmber
import com.example.e_commerce.ui.theme.NeonCyan
import com.example.e_commerce.ui.theme.NeonIndigo
import com.example.e_commerce.ui.theme.NeonLime
import com.example.e_commerce.ui.theme.NeonPink
import com.example.e_commerce.ui.theme.TextSecondary
import java.text.DateFormat
import java.util.Date

@Composable
fun DashboardScreen(
    onOpenRoutines: () -> Unit,
    onOpenLibrary: () -> Unit,
    onOpenBodyWeight: () -> Unit,
    onOpenAiCoach: () -> Unit = {},
    viewModel: DashboardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item { HeroGreeting(state.profile, state.streak) }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                StreakGlassCard(state.streak, modifier = Modifier.weight(1f))
                TodayGlassCard(state.todayWorkouts, modifier = Modifier.weight(1f))
            }
        }

        item {
            QuickActionsRow(
                onRoutines = onOpenRoutines,
                onLibrary = onOpenLibrary,
                onBodyWeight = onOpenBodyWeight,
                onAiCoach = onOpenAiCoach
            )
        }

        if (state.personalRecords.isNotEmpty()) {
            item { SectionTitle("Personal records", "Your heaviest sets") }
            items(items = state.personalRecords, key = { it.exerciseName }) { pr ->
                PrGlassCard(pr, state.profile?.weightUnit ?: WeightUnit.KG)
            }
        }

        item { SectionTitle("Body weight", "Trends over time") }
        item {
            BodyWeightGlassCard(
                currentKg = state.latestWeight?.weightKg,
                startingKg = state.profile?.startingWeightKg ?: 0f,
                unit = state.profile?.weightUnit ?: WeightUnit.KG,
                onClick = onOpenBodyWeight
            )
        }

        if (state.recentWorkouts.isNotEmpty()) {
            item { SectionTitle("Recent sessions", null) }
            items(items = state.recentWorkouts, key = { it.id }) { w -> RecentWorkoutGlass(w) }
        }

        item { Spacer(Modifier.height(96.dp)) } // floating nav clearance
    }
}

// ===== Hero =====

@Composable
private fun HeroGreeting(profile: UserProfileEntity?, streak: Int) {
    val greeting = when (java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) {
        in 5..11 -> "Good morning"
        in 12..17 -> "Afternoon"
        in 18..21 -> "Evening"
        else -> "Late grind"
    }
    val name = profile?.name?.takeIf { it.isNotBlank() } ?: "athlete"

    Column {
        Text(
            greeting,
            style = MaterialTheme.typography.titleMedium,
            color = TextSecondary,
            fontWeight = FontWeight.Medium
        )
        GradientText(
            text = name,
            style = MaterialTheme.typography.displayLarge,
            gradient = Gradients.Primary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = when {
                streak == 0 -> "Fresh start. Put one on the board today."
                streak == 1 -> "Day 1 of a new streak. Keep the chain alive."
                else -> "$streak days in a row. Don't break the chain."
            },
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
    }
}

// Approximates gradient text: render white text, clip a gradient on top via
// a layered Box. Works on all API levels without custom Paint shaders.
@Composable
private fun GradientText(
    text: String,
    style: androidx.compose.ui.text.TextStyle,
    gradient: Brush
) {
    Box {
        // base layer (accessibility + shape)
        Text(text, style = style, color = Color.Transparent)
        // foreground: paint text with a brush
        Text(
            text = text,
            style = style.copy(
                brush = gradient,
                fontWeight = style.fontWeight
            )
        )
    }
}

// ===== Hero cards =====

@Composable
private fun StreakGlassCard(streak: Int, modifier: Modifier) {
    // Progress toward "flame milestone" — every 7 days fills the ring.
    val progress = ((streak % 7).toFloat() / 7f).coerceAtLeast(if (streak > 0) 0.04f else 0f)
    val animated by animateFloatAsState(targetValue = streak.toFloat(), label = "streak")

    GlassCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StatRing(
                progress = progress,
                size = 112.dp,
                gradient = Gradients.Fire
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = NeonAmber,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = animated.toInt().toString(),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                if (streak == 1) "day streak" else "day streak",
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun TodayGlassCard(todayWorkouts: List<Workout>, modifier: Modifier) {
    val totalSets = todayWorkouts.sumOf { it.sets.size }
    val totalVolume = todayWorkouts.sumOf { it.totalVolume.toDouble() }.toInt()

    GlassCard(modifier = modifier) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                "TODAY",
                style = MaterialTheme.typography.labelMedium,
                color = NeonCyan,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                totalSets.toString(),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "sets logged",
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary
            )
            Spacer(Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.ArrowOutward,
                    contentDescription = null,
                    tint = NeonLime,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    "${totalVolume}kg volume",
                    style = MaterialTheme.typography.labelLarge,
                    color = NeonLime,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ===== Quick actions =====

@Composable
private fun QuickActionsRow(
    onRoutines: () -> Unit,
    onLibrary: () -> Unit,
    onBodyWeight: () -> Unit,
    onAiCoach: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        QuickActionChip("Routines", gradient = Gradients.Cool, modifier = Modifier.weight(1f), onClick = onRoutines)
        QuickActionChip("Library", gradient = Gradients.Primary, modifier = Modifier.weight(1f), onClick = onLibrary)
        QuickActionChip("Weigh-in", gradient = Gradients.Lime, modifier = Modifier.weight(1f), onClick = onBodyWeight)
        QuickActionChip("AI coach", gradient = Gradients.Fire, modifier = Modifier.weight(1f), onClick = onAiCoach)
    }
}

@Composable
private fun QuickActionChip(
    label: String,
    gradient: Brush,
    modifier: Modifier,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(gradient)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ===== Section helpers =====

@Composable
private fun SectionTitle(title: String, subtitle: String?) {
    Column(modifier = Modifier.padding(top = 4.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (subtitle != null) {
            Text(
                subtitle,
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary
            )
        }
    }
}

// ===== PR, Body Weight, Recent =====

@Composable
private fun PrGlassCard(pr: PersonalRecord, unit: WeightUnit) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Gradients.Primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Spacer(Modifier.size(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    pr.exerciseName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "${pr.bestReps} reps · ${pr.category.displayName}",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    unit.format(pr.bestWeightKg),
                    style = MaterialTheme.typography.titleLarge,
                    color = NeonLime,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "PR",
                    style = MaterialTheme.typography.labelMedium,
                    color = NeonPink,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

@Composable
private fun BodyWeightGlassCard(
    currentKg: Float?,
    startingKg: Float,
    unit: WeightUnit,
    onClick: () -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Gradients.Cool),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.MonitorWeight,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Spacer(Modifier.size(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    if (currentKg == null) "Log first weigh-in"
                    else unit.format(currentKg),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                if (currentKg != null && startingKg > 0f) {
                    val diff = currentKg - startingKg
                    val arrow = if (diff >= 0f) "▲" else "▼"
                    val color = if (diff < 0f) NeonLime else NeonCyan
                    Text(
                        "$arrow ${unit.format(kotlin.math.abs(diff))} since start",
                        style = MaterialTheme.typography.labelLarge,
                        color = color
                    )
                } else {
                    Text(
                        "Tap to track your progress",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary
                    )
                }
            }
            Text("→", style = MaterialTheme.typography.headlineMedium, color = NeonIndigo)
        }
    }
}

@Composable
private fun RecentWorkoutGlass(workout: Workout) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Gradients.Primary)
                )
                Spacer(Modifier.size(10.dp))
                Text(
                    workout.exerciseName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    DateFormat.getDateInstance(DateFormat.SHORT)
                        .format(Date(workout.performedAtMillis)),
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = "${workout.sets.size} sets · ${workout.totalVolume.toInt()} kg volume",
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary,
                textAlign = TextAlign.Start
            )
        }
    }
}