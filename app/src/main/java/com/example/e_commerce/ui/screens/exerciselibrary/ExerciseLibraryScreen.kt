package com.example.e_commerce.ui.screens.exerciselibrary

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_commerce.data.catalog.Exercise
import com.example.e_commerce.data.local.ExerciseCategory
import com.example.e_commerce.ui.AppViewModelProvider
import com.example.e_commerce.ui.components.ExercisePreviewSheet

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ExerciseLibraryScreen(
    onBack: () -> Unit,
    // When this callback is non-null, the screen is a picker and shows
    // "Choose" buttons. When null, tapping an exercise just opens the detail.
    onPick: ((Exercise) -> Unit)? = null,
    viewModel: ExerciseLibraryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var detail by remember { mutableStateOf<Exercise?>(null) }
    // Sibling state so the preview sheet layers on top of the detail sheet
    // rather than nesting inside it — cleaner z-order.
    var previewFor by remember { mutableStateOf<Exercise?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (onPick != null) "Pick an exercise" else "Exercise library") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { inner ->
        Column(modifier = Modifier.fillMaxSize().padding(inner)) {
            OutlinedTextField(
                value = state.query,
                onValueChange = viewModel::onQueryChange,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text("Search exercises or muscles") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                FilterChip(
                    selected = state.filter == null,
                    onClick = { viewModel.onFilterChange(null) },
                    label = { Text("All") }
                )
                ExerciseCategory.entries.forEach { cat ->
                    FilterChip(
                        selected = state.filter == cat,
                        onClick = { viewModel.onFilterChange(cat) },
                        label = { Text(cat.displayName) }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items = state.results, key = { it.id }) { exercise ->
                    ExerciseRow(
                        exercise = exercise,
                        pickable = onPick != null,
                        onOpen = { detail = exercise },
                        onPick = { onPick?.invoke(exercise) }
                    )
                }
            }
        }
    }

    if (detail != null) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { detail = null },
            sheetState = sheetState
        ) {
            ExerciseDetailSheet(
                exercise = detail!!,
                pickable = onPick != null,
                onPick = {
                    val picked = detail!!
                    detail = null
                    onPick?.invoke(picked)
                },
                onWatchVideo = {
                    previewFor = detail
                }
            )
        }
    }

    if (previewFor != null) {
        ExercisePreviewSheet(
            exercise = previewFor!!,
            onDismiss = { previewFor = null }
        )
    }
}

@Composable
private fun ExerciseRow(
    exercise: Exercise,
    pickable: Boolean,
    onOpen: () -> Unit,
    onPick: () -> Unit
) {
    OutlinedCard(shape = RoundedCornerShape(14.dp), onClick = onOpen) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(exercise.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Text(
                    "${exercise.category.displayName} · ${exercise.primaryMuscles.joinToString()}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                AssistChip(
                    onClick = {},
                    label = { Text(exercise.difficulty.label) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)
                    )
                )
            }
            if (pickable) {
                Button(onClick = onPick) { Text("Pick") }
            }
        }
    }
}

@Composable
private fun ExerciseDetailSheet(
    exercise: Exercise,
    pickable: Boolean,
    onPick: () -> Unit,
    onWatchVideo: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(exercise.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(
            "${exercise.category.displayName} · ${exercise.difficulty.label} · ${exercise.equipment}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
        )
        Text("Primary muscles: ${exercise.primaryMuscles.joinToString()}")

        // Free YouTube demo — primary action so new users see it first.
        OutlinedButton(
            onClick = onWatchVideo,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.PlayCircle, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(
                if (exercise.videoId != null) "Watch demo video"
                else "Watch free YouTube tutorial"
            )
        }

        Text("How to do it", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        exercise.instructions.forEachIndexed { i, step ->
            Text("${i + 1}. $step", style = MaterialTheme.typography.bodyLarge)
        }

        if (exercise.tips.isNotEmpty()) {
            Text("Form tips", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            exercise.tips.forEach { tip ->
                Text("• $tip", style = MaterialTheme.typography.bodyLarge)
            }
        }

        if (pickable) {
            Button(onClick = onPick, modifier = Modifier.fillMaxWidth()) {
                Text("Use this exercise")
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}