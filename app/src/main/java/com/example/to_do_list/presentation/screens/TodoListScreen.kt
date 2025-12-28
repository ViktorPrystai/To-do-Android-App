package com.example.to_do_list.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.to_do_list.presentation.components.PriorityBadge
import com.example.to_do_list.presentation.components.PriorityChip
import com.example.to_do_list.data.models.Task
import com.example.to_do_list.data.models.Priority
import com.example.to_do_list.ui.theme.*
import com.example.to_do_list.presentation.viewmodel.TaskFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    tasks: List<Task>,
    currentFilter: TaskFilter = TaskFilter.ALL,
    onAddTask: (String, Priority) -> Unit,
    onTaskClick: (Task) -> Unit,
    onFilterChange: (TaskFilter) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var taskText by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }

    Column(
        modifier = modifier.fillMaxSize()
//            .background(Blue)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Blue,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .windowInsetsPadding(WindowInsets.displayCutout)
            ) {
                Text(
                    text = "My Tasks",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${tasks.count { it.isCompleted }} of ${tasks.size} completed",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        // Filter
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
            shadowElevation = 1.dp
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        filter = TaskFilter.ALL,
                        label = "All",
                        isSelected = currentFilter == TaskFilter.ALL,
                        onClick = { onFilterChange(TaskFilter.ALL) }
                    )
                }
                item {
                    FilterChip(
                        filter = TaskFilter.ACTIVE,
                        label = "Active",
                        isSelected = currentFilter == TaskFilter.ACTIVE,
                        onClick = { onFilterChange(TaskFilter.ACTIVE) }
                    )
                }
                item {
                    FilterChip(
                        filter = TaskFilter.COMPLETED,
                        label = "Completed",
                        isSelected = currentFilter == TaskFilter.COMPLETED,
                        onClick = { onFilterChange(TaskFilter.COMPLETED) }
                    )
                }
                item {
                    Text(
                        text = "|",
                        color = LightGray,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
                item {
                    FilterChip(
                        filter = TaskFilter.PRIORITY_CRITICAL,
                        label = "Critical",
                        isSelected = currentFilter == TaskFilter.PRIORITY_CRITICAL,
                        onClick = { onFilterChange(TaskFilter.PRIORITY_CRITICAL) },
                        color = PriorityCritical
                    )
                }
                item {
                    FilterChip(
                        filter = TaskFilter.PRIORITY_HIGH,
                        label = "High",
                        isSelected = currentFilter == TaskFilter.PRIORITY_HIGH,
                        onClick = { onFilterChange(TaskFilter.PRIORITY_HIGH) },
                        color = PriorityHigh
                    )
                }
                item {
                    FilterChip(
                        filter = TaskFilter.PRIORITY_MEDIUM,
                        label = "Medium",
                        isSelected = currentFilter == TaskFilter.PRIORITY_MEDIUM,
                        onClick = { onFilterChange(TaskFilter.PRIORITY_MEDIUM) },
                        color = PriorityMedium
                    )
                }
                item {
                    FilterChip(
                        filter = TaskFilter.PRIORITY_LOW,
                        label = "Low",
                        isSelected = currentFilter == TaskFilter.PRIORITY_LOW,
                        onClick = { onFilterChange(TaskFilter.PRIORITY_LOW) },
                        color = PriorityLow
                    )
                }
            }
        }

        // Input
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = taskText,
                        onValueChange = { taskText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Add a new task...") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Blue,
                            unfocusedBorderColor = LightGray
                        )
                    )
                    FloatingActionButton(
                        onClick = {
                            if (taskText.isNotBlank()) {
                                onAddTask(taskText, selectedPriority)
                                taskText = ""
                                selectedPriority = Priority.MEDIUM
                            }
                        },
                        containerColor = Blue,
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add task")
                    }
                }

                // Priority
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Priority:",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        color = Gray
                    )
                    Priority.entries.forEach { priority ->
                        PriorityChip(
                            priority = priority,
                            isSelected = selectedPriority == priority,
                            onClick = { selectedPriority = priority }
                        )
                    }
                }
            }
        }

        // Tasks
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onClick = { onTaskClick(task) }
                )
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        border = if (task.isCompleted) BorderStroke(2.dp, Green.copy(alpha = 0.3f)) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TaskCheckbox(isCompleted = task.isCompleted)

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.bodyLarge,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                        color = if (task.isCompleted) Gray else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    PriorityBadge(priority = task.priority)
                }
                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = LightGray,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

@Composable
fun TaskCheckbox(isCompleted: Boolean) {
    val color = if (isCompleted) Green else Gray

    Box(
        modifier = Modifier.size(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw circle
            drawCircle(
                color = color,
                radius = size.minDimension / 2,
                style = Stroke(width = 3.dp.toPx())
            )

            if (isCompleted) {
                val checkPath = Path().apply {
                    moveTo(size.width * 0.25f, size.height * 0.5f)
                    lineTo(size.width * 0.45f, size.height * 0.7f)
                    lineTo(size.width * 0.75f, size.height * 0.3f)
                }
                drawPath(
                    path = checkPath,
                    color = color,
                    style = Stroke(
                        width = 3.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
    }
}

@Composable
fun FilterChip(
    filter: TaskFilter,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    color: Color = Blue
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) color else Color.Transparent,
        border = BorderStroke(1.dp, color)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) Color.White else color,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListScreenPreview() {
    Todo_listTheme {
        TodoListScreen(
            tasks = listOf(
                Task(id = 1, title = "Complete project proposal", description = "Include budget and timeline", isCompleted = true, priority = Priority.HIGH),
                Task(id = 2, title = "Review code changes", description = "Check PR #123", isCompleted = true, priority = Priority.MEDIUM),
                Task(id = 3, title = "Schedule team meeting", description = "", isCompleted = false, priority = Priority.CRITICAL)
            ),
            currentFilter = TaskFilter.ALL,
            onAddTask = { _, _ -> },
            onTaskClick = {},
            onFilterChange = {}
        )
    }
}