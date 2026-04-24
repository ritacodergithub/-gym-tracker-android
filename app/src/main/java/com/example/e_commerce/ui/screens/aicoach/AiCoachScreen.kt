package com.example.e_commerce.ui.screens.aicoach

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_commerce.ui.AppViewModelProvider
import com.example.e_commerce.ui.components.GlassCard
import com.example.e_commerce.ui.components.GradientButton
import com.example.e_commerce.ui.components.PageBackground
import com.example.e_commerce.ui.theme.Gradients
import com.example.e_commerce.ui.theme.NeonCyan
import com.example.e_commerce.ui.theme.NeonLime
import com.example.e_commerce.ui.theme.NeonRose
import com.example.e_commerce.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiCoachScreen(
    onBack: () -> Unit,
    viewModel: AiCoachViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle(initialValue = null)

    PageBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("AI coach") },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
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
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HeroCard(goal = state.goal.label)

                GradientButton(
                    text = if (state.loading) "Thinking..." else "Generate today's plan",
                    onClick = { viewModel.generateTodaysPlan() },
                    enabled = !state.loading,
                    icon = Icons.Default.AutoAwesome,
                    modifier = Modifier.fillMaxWidth()
                )

                GradientButton(
                    text = "Give me a motivational push",
                    gradient = Gradients.Lime,
                    onClick = {
                        viewModel.generateMotivation(
                            streak = 0, // Dashboard owns the real value; stub for this screen
                            name = profile?.name ?: "athlete"
                        )
                    },
                    enabled = !state.loading,
                    modifier = Modifier.fillMaxWidth()
                )

                when {
                    state.needsApiKey -> ApiKeyHint()
                    state.loading -> LoadingCard()
                    state.error != null -> ErrorCard(state.error!!)
                    state.result != null -> ResultCard(state.result!!)
                    else -> EmptyHint()
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun HeroCard(goal: String) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonCyan)
                Spacer(Modifier.height(0.dp).fillMaxWidth(0f))
                Spacer(Modifier.fillMaxWidth(0f))
                Text(
                    "  POWERED BY GEMINI",
                    style = MaterialTheme.typography.labelMedium,
                    color = NeonCyan
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                "Your goal: $goal",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "Tap a button to generate a fresh workout or motivation, tuned to your profile. Free via Google's Gemini API — you only pay if you exceed the generous free tier.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun LoadingCard() {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                color = NeonCyan,
                strokeWidth = 3.dp,
                modifier = Modifier.height(24.dp)
            )
            Spacer(Modifier.fillMaxWidth(0f))
            Text(
                "  Coach is writing your plan…",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ResultCard(text: String) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp)) {
            Text(
                "YOUR SESSION",
                style = MaterialTheme.typography.labelMedium,
                color = NeonLime
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ErrorCard(message: String) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp)) {
            Text(
                "SOMETHING WENT WRONG",
                style = MaterialTheme.typography.labelMedium,
                color = NeonRose
            )
            Text(
                message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ApiKeyHint() {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp)) {
            Text(
                "API KEY NEEDED",
                style = MaterialTheme.typography.labelMedium,
                color = NeonRose
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Get a free Gemini key at ai.google.dev, then add this line " +
                    "to your local.properties file (same folder as settings.gradle.kts) " +
                    "and rebuild:\n\n" +
                    "gemini.api.key=YOUR_KEY_HERE",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun EmptyHint() {
    Text(
        "Generate a plan above and it'll appear here.",
        style = MaterialTheme.typography.labelLarge,
        color = TextSecondary,
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}