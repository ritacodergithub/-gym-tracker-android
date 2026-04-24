package com.example.e_commerce.ui.screens.addworkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_commerce.data.local.ExerciseCategory
import com.example.e_commerce.domain.model.Workout
import com.example.e_commerce.ui.AppViewModelProvider
import com.example.e_commerce.ui.components.RestTimerSheet
import com.example.e_commerce.ui.screens.exerciselibrary.ExerciseLibraryScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    prefilledExerciseId: String? = null,
    viewModel: AddWorkoutViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHost = remember { SnackbarHostState() }
    var showPicker by remember { mutableStateOf(false) }
    var showRestTimer by remember { mutableStateOf(false) }

    LaunchedEffect(prefilledExerciseId) {
        prefilledExerciseId?.let { viewModel.prefillFromExerciseId(it) }
    }
    LaunchedEffect(state.error) {
        state.error?.let { snackbarHost.showSnackbar(it) }
    }
    LaunchedEffect(state.saved) {
        if (state.saved) {
            viewModel.consumeSaved()
            onSaved()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New workout") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { showPicker = true }) {
                        Icon(Icons.Default.Search, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Library")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showRestTimer = true }) {
                Icon(Icons.Default.Timer, contentDescription = "Rest timer")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHost) }
    ) { inner ->
        AddWorkoutForm(
            state = state,
            onExerciseNameChange = viewModel::onExerciseNameChange,
            onCategoryChange = viewModel::onCategoryChange,
            onNotesChange = viewModel::onNotesChange,
            onAddSet = viewModel::addSet,
            onRemoveSet = viewModel::removeSet,
            onRepsChange = viewModel::onSetRepsChange,
            onWeightChange = viewModel::onSetWeightChange,
            onSave = viewModel::save,
            contentPadding = inner
        )
    }

    if (showPicker) {
        // The picker reuses the full library screen — passing onPick turns it
        // into a modal selector. Cheap reuse.
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showPicker = false },
            properties = androidx.compose.ui.window.DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            ExerciseLibraryScreen(
                onBack = { showPicker = false },
                onPick = {
                    viewModel.applyLibraryExercise(it)
                    showPicker = false
                }
            )
        }
    }

    if (showRestTimer) {
        RestTimerSheet(onDismiss = { showRestTimer = false })
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddWorkoutForm(
    state: AddWorkoutViewModel.UiState,
    onExerciseNameChange: (String) -> Unit,
    onCategoryChange: (ExerciseCategory) -> Unit,
    onNotesChange: (String) -> Unit,
    onAddSet: () -> Unit,
    onRemoveSet: (Long) -> Unit,
    onRepsChange: (Long, String) -> Unit,
    onWeightChange: (Long, String) -> Unit,
    onSave: () -> Unit,
    contentPadding: PaddingValues
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = contentPadding.calculateTopPadding() + 8.dp,
            bottom = contentPadding.calculateBottomPadding() + 96.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedTextField(
                value = state.exerciseName,
                onValueChange = onExerciseNameChange,
                label = { Text("Exercise (tap Library for suggestions)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (state.lastWorkoutHint != null) {
            item { LastTimeHint(state.lastWorkoutHint) }
        }

        item { CategoryChips(state.category, onCategoryChange) }

        item {
            Text(
                "Sets",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(state.sets, key = { it.uiId }) { set ->
            SetRow(
                index = state.sets.indexOf(set) + 1,
                set = set,
                onRepsChange = { onRepsChange(set.uiId, it) },
                onWeightChange = { onWeightChange(set.uiId, it) },
                onDelete = { onRemoveSet(set.uiId) }
            )
        }

        item {
            OutlinedButton(
                onClick = onAddSet,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Add set")
            }
        }

        item {
            OutlinedTextField(
                value = state.notes,
                onValueChange = onNotesChange,
                label = { Text("Notes (optional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Button(
                onClick = onSave,
                enabled = !state.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(18.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(12.dp))
                }
                Text("Save workout")
            }
        }
    }
}

@Composable
private fun LastTimeHint(workout: Workout) {
    val summary = workout.sets.joinToString(" · ") { "${it.reps}×${weight(it.weightKg)}" }
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                "Last time",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(summary, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

private fun weight(kg: Float): String =
    if (kg % 1f == 0f) "${kg.toInt()}kg" else "%.1fkg".format(kg)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoryChips(
    selected: ExerciseCategory,
    onSelect: (ExerciseCategory) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ExerciseCategory.entries.forEach { cat ->
            FilterChip(
                selected = cat == selected,
                onClick = { onSelect(cat) },
                label = { Text(cat.displayName) }
            )
        }
    }
}

@Composable
private fun SetRow(
    index: Int,
    set: AddWorkoutViewModel.SetInput,
    onRepsChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    OutlinedCard(shape = RoundedCornerShape(12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssistChip(
                onClick = {},
                label = { Text("Set $index") },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
            OutlinedTextField(
                value = set.reps,
                onValueChange = onRepsChange,
                label = { Text("Reps") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = set.weight,
                onValueChange = onWeightChange,
                label = { Text("kg") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove set",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}