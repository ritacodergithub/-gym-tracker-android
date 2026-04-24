package com.example.e_commerce.ui.screens.routines

import androidx.lifecycle.ViewModel
import com.example.e_commerce.data.catalog.Routine
import com.example.e_commerce.data.catalog.RoutineLibrary

class RoutinesViewModel : ViewModel() {
    val routines: List<Routine> = RoutineLibrary.all
}