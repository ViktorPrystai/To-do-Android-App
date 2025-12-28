package com.example.to_do_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.to_do_list.data.models.Task
import com.example.to_do_list.presentation.screens.TaskDetailScreen
import com.example.to_do_list.presentation.screens.TodoListScreen
import com.example.to_do_list.ui.theme.Todo_listTheme
import com.example.to_do_list.presentation.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Todo_listTheme {
                TodoApp()
            }
        }
    }
}

@Composable
fun TodoApp(viewModel: TaskViewModel = viewModel()) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val currentFilter by viewModel.currentFilter.collectAsStateWithLifecycle()
    var selectedTask by remember { mutableStateOf<Task?>(null) }

    LaunchedEffect(tasks) {
        selectedTask?.let { selected ->
            selectedTask = tasks.find { it.id == selected.id }
        }
    }

    if (selectedTask == null) {
        TodoListScreen(
            tasks = tasks,
            currentFilter = currentFilter,
            onAddTask = { title, priority ->
                viewModel.addTask(title, priority)
            },
            onTaskClick = { task ->
                selectedTask = task
            },
            onFilterChange = { filter ->
                viewModel.setFilter(filter)
            }
        )
    } else {
        TaskDetailScreen(
            task = selectedTask!!,
            onBack = {
                selectedTask = null
            },
            onDelete = {
                viewModel.deleteTask(selectedTask!!)
                selectedTask = null
            },
            onSave = { description, isCompleted, priority ->
                val updatedTask = selectedTask!!.copy(
                    description = description,
                    isCompleted = isCompleted,
                    priority = priority
                )
                viewModel.updateTask(updatedTask)
            }
        )
    }
}