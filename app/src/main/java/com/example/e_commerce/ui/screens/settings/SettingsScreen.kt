package com.example.e_commerce.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_commerce.data.local.Goal
import com.example.e_commerce.data.local.WeightUnit
import com.example.e_commerce.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    onResetToOnboarding: () -> Unit,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(topBar = { TopAppBar(title = { Text("Settings") }) }) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            profile?.let { p ->
                SectionCard("Profile") {
                    Text(p.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    if (p.email.isNotBlank()) {
                        Text(p.email, style = MaterialTheme.typography.labelLarge)
                    }
                    Text(
                        "Starting weight: ${p.weightUnit.format(p.startingWeightKg)}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                    )
                }

                SectionCard("Units") {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        WeightUnit.entries.forEach { u ->
                            FilterChip(
                                selected = p.weightUnit == u,
                                onClick = { viewModel.updateUnit(u) },
                                label = { Text(u.label.uppercase()) }
                            )
                        }
                    }
                }

                SectionCard("Goal") {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Goal.entries.forEach { g ->
                            FilterChip(
                                selected = p.goal == g,
                                onClick = { viewModel.updateGoal(g) },
                                label = { Text(g.label) }
                            )
                        }
                    }
                }

                SectionCard("Daily reminder") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Nudge me at", modifier = Modifier.weight(1f))
                        Switch(
                            checked = p.reminderEnabled,
                            onCheckedChange = { viewModel.updateReminder(it, p.reminderHour) }
                        )
                    }
                    if (p.reminderEnabled) {
                        var hour by remember(p.reminderHour) { mutableStateOf(p.reminderHour.toFloat()) }
                        Text("${hour.toInt()}:00", style = MaterialTheme.typography.titleLarge)
                        Slider(
                            value = hour,
                            onValueChange = { hour = it },
                            onValueChangeFinished = {
                                viewModel.updateReminder(true, hour.toInt())
                            },
                            valueRange = 6f..22f,
                            steps = 15
                        )
                        Text(
                            "Scheduling uses WorkManager — wire up a ReminderWorker when you're ready.",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            SectionCard("Danger zone") {
                OutlinedButton(
                    onClick = { viewModel.clearWorkouts() },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Clear all workouts") }
                OutlinedButton(
                    onClick = { viewModel.clearBodyWeight() },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Clear body-weight history") }
                Button(
                    onClick = { showResetDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Reset app (clear everything)") }
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset app?") },
            text = { Text("This wipes your profile, workouts, and body-weight history and sends you back to onboarding. This can't be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showResetDialog = false
                    viewModel.resetEverything()
                    onResetToOnboarding()
                }) { Text("Reset", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            content()
        }
    }
}