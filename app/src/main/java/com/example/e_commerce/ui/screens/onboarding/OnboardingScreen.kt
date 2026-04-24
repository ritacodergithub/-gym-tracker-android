package com.example.e_commerce.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_commerce.data.local.Goal
import com.example.e_commerce.ui.AppViewModelProvider
import com.example.e_commerce.ui.components.GradientButton
import com.example.e_commerce.ui.components.PageBackground
import com.example.e_commerce.ui.theme.Gradients
import com.example.e_commerce.ui.theme.NeonCyan
import com.example.e_commerce.ui.theme.NeonIndigo
import com.example.e_commerce.ui.theme.NeonLime
import com.example.e_commerce.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.done) { if (state.done) onFinished() }

    PageBackground {
        Scaffold(containerColor = Color.Transparent) { inner ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .padding(horizontal = 28.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Spacer(Modifier.height(24.dp))

                // Neon logo badge
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Gradients.Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Bolt,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))
                HeroTitle()

                Spacer(Modifier.height(16.dp))

                GlassTextField(
                    value = state.name,
                    onValueChange = viewModel::onNameChange,
                    label = "Your name",
                    error = state.errors[OnboardingViewModel.Field.NAME]
                )
                GlassTextField(
                    value = state.email,
                    onValueChange = viewModel::onEmailChange,
                    label = "Email (optional)",
                    keyboardType = KeyboardType.Email
                )
                GlassTextField(
                    value = state.weight,
                    onValueChange = viewModel::onWeightChange,
                    label = "Current body weight (kg)",
                    keyboardType = KeyboardType.Decimal,
                    error = state.errors[OnboardingViewModel.Field.WEIGHT]
                )

                Spacer(Modifier.height(4.dp))
                Text(
                    "Pick a goal",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Goal.entries.forEach { g ->
                        FilterChip(
                            selected = g == state.goal,
                            onClick = { viewModel.onGoalChange(g) },
                            label = { Text(g.label) },
                            shape = RoundedCornerShape(14.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0x1FFFFFFF),
                                labelColor = TextSecondary,
                                selectedContainerColor = NeonIndigo,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                GradientButton(
                    text = if (state.isSaving) "Saving…" else "Start training",
                    onClick = viewModel::finish,
                    enabled = !state.isSaving,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "You can change everything in Settings later.",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun HeroTitle() {
    Column {
        Text(
            "Welcome to",
            style = MaterialTheme.typography.titleLarge,
            color = TextSecondary,
            fontWeight = FontWeight.Medium
        )
        Box {
            Text(
                text = "Gym Tracker.",
                style = MaterialTheme.typography.displayLarge.copy(
                    brush = Gradients.Primary
                ),
                fontWeight = FontWeight.Black
            )
        }
        Text(
            "AI coach. Smart logging. Zero fluff.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
    }
}

@Composable
private fun GlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = error != null,
        supportingText = { error?.let { Text(it) } },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(16.dp),
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
}