package com.example.to_do_list.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.to_do_list.data.models.Priority
import com.example.to_do_list.ui.theme.PriorityCritical
import com.example.to_do_list.ui.theme.PriorityHigh
import com.example.to_do_list.ui.theme.PriorityLow
import com.example.to_do_list.ui.theme.PriorityMedium

fun getPriorityColor(priority: Priority): Color {
    return when (priority) {
        Priority.LOW -> PriorityLow
        Priority.MEDIUM -> PriorityMedium
        Priority.HIGH -> PriorityHigh
        Priority.CRITICAL -> PriorityCritical
    }
}

@Composable
fun PriorityChip(
    priority: Priority,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val priorityColor = getPriorityColor(priority)

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) priorityColor else Color.Transparent,
        border = BorderStroke(1.dp, priorityColor)
    ) {
        Text(
            text = priority.displayName,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) Color.White else priorityColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun PriorityBadge(priority: Priority) {
    val priorityColor = getPriorityColor(priority)

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = priorityColor.copy(alpha = 0.2f),
        border = BorderStroke(1.dp, priorityColor)
    ) {
        Text(
            text = priority.displayName,
            style = MaterialTheme.typography.labelSmall,
            color = priorityColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
