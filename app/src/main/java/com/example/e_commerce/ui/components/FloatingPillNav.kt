package com.example.e_commerce.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.e_commerce.ui.theme.Gradients
import com.example.e_commerce.ui.theme.TextSecondary

data class NavTab(
    val route: String,
    val label: String,
    val icon: ImageVector
)

// Floating pill-shaped nav bar: dark glass background, selected tab expands
// with gradient + label, others stay as icon-only. Feels more modern than
// the standard NavigationBar and matches the glassmorphism theme.
@Composable
fun FloatingPillNav(
    tabs: List<NavTab>,
    selected: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0xE6101330))
                .border(1.dp, Gradients.GlassBorder, RoundedCornerShape(28.dp))
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val isSelected = tab.route == selected
                PillItem(
                    tab = tab,
                    selected = isSelected,
                    modifier = Modifier.weight(if (isSelected) 1.6f else 1f),
                    onClick = { onSelect(tab.route) }
                )
            }
        }
    }
}

@Composable
private fun PillItem(
    tab: NavTab,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val background: Brush = if (selected) Gradients.Primary else SolidColor(Color.Transparent)
    val iconColor by animateColorAsState(
        targetValue = if (selected) Color.White else TextSecondary,
        label = "tabIcon"
    )

    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(background)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(tab.icon, contentDescription = tab.label, tint = iconColor)
            if (selected) {
                Spacer(Modifier.width(6.dp))
                Text(
                    tab.label,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }
        }
    }
}