package com.example.e_commerce.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.e_commerce.ui.theme.Gradients

// Ambient gradient wrapper — apply once at the Scaffold level so every
// subsequent surface can stay transparent and the gradient bleeds through.
@Composable
fun PageBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = Gradients.pageBackdrop())
    ) {
        content()
    }
}