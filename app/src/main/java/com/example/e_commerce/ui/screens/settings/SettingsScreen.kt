package com.example.e_commerce.ui.screens.settings

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_commerce.data.local.Goal
import com.example.e_commerce.data.local.WeightUnit
import com.example.e_commerce.ui.AppViewModelProvider
import com.example.e_commerce.ui.components.GlassCard
import com.example.e_commerce.ui.components.GradientButton
import com.example.e_commerce.ui.theme.Gradients
import com.example.e_commerce.ui.theme.NeonCyan
import com.example.e_commerce.ui.theme.NeonIndigo
import com.example.e_commerce.ui.theme.NeonLime
import com.example.e_commerce.ui.theme.NeonRose
import com.example.e_commerce.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    onResetToOnboarding: () -> Unit,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    var showResetDialog by remember { mutableStateOf(false) }

    // Android 13+ POST_NOTIFICATIONS — requested when the user turns on
    // the reminder switch. On pre-13, the permission is granted by install.
    val notificationPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { /* best-effort; no action if denied */ }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Settings",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }

        profile?.let { p ->
            item {
                SectionCard("Profile") {
                    Text(
                        p.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (p.email.isNotBlank()) {
                        Text(p.email, style = MaterialTheme.typography.labelLarge, color = TextSecondary)
                    }
                    Text(
                        "Starting weight: ${p.weightUnit.format(p.startingWeightKg)}",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary
                    )
                }
            }
            item {
                SectionCard("Units") {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        WeightUnit.entries.forEach { u ->
                            FilterChip(
                                selected = p.weightUnit == u,
                                onClick = { viewModel.updateUnit(u) },
                                label = { Text(u.label.uppercase()) },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color(0x1FFFFFFF),
                                    labelColor = TextSecondary,
                                    selectedContainerColor = NeonIndigo,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
            item {
                SectionCard("Goal") {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Goal.entries.forEach { g ->
                            FilterChip(
                                selected = p.goal == g,
                                onClick = { viewModel.updateGoal(g) },
                                label = { Text(g.label) },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color(0x1FFFFFFF),
                                    labelColor = TextSecondary,
                                    selectedContainerColor = NeonIndigo,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
            item {
                SectionCard("Daily reminder") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Nudge me every day",
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Switch(
                            checked = p.reminderEnabled,
                            onCheckedChange = { enabled ->
                                if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                                viewModel.updateReminder(enabled, p.reminderHour)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = NeonIndigo
                            )
                        )
                    }
                    if (p.reminderEnabled) {
                        var hour by remember(p.reminderHour) { mutableStateOf(p.reminderHour.toFloat()) }
                        Text(
                            "${hour.toInt()}:00",
                            style = MaterialTheme.typography.displayMedium,
                            color = NeonLime,
                            fontWeight = FontWeight.Bold
                        )
                        Slider(
                            value = hour,
                            onValueChange = { hour = it },
                            onValueChangeFinished = {
                                viewModel.updateReminder(true, hour.toInt())
                            },
                            valueRange = 6f..22f,
                            steps = 15
                        )
                    }
                }
            }
        }

        item {
            SectionCard("Danger zone", accent = NeonRose) {
                OutlinedButton(
                    onClick = { viewModel.clearWorkouts() },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Clear all workouts") }
                OutlinedButton(
                    onClick = { viewModel.clearBodyWeight() },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Clear body-weight history") }
                GradientButton(
                    text = "Reset app (wipe everything)",
                    onClick = { showResetDialog = true },
                    gradient = Gradients.Fire,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item { Spacer(Modifier.height(96.dp)) }
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
                }) { Text("Reset", color = NeonRose) }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun SectionCard(
    title: String,
    accent: Color = NeonCyan,
    content: @Composable () -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                title.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = accent,
                fontWeight = FontWeight.Bold
            )
            content()
        }
    }
}