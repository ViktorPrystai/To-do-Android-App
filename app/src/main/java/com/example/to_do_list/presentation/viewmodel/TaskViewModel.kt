package com.example.to_do_list.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do_list.data.models.Priority
import com.example.to_do_list.data.models.Task
import com.example.to_do_list.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TaskFilter {
    ALL,
    PRIORITY_LOW,
    PRIORITY_MEDIUM,
    PRIORITY_HIGH,
    PRIORITY_CRITICAL,
    COMPLETED,
    ACTIVE
}

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _currentFilter = MutableStateFlow(TaskFilter.ALL)
    val currentFilter: StateFlow<TaskFilter> = _currentFilter

    private var loadTasksJob: Job? = null

    init {
        loadTasks()
    }

    private fun loadTasks() {
        loadTasksJob?.cancel()

        loadTasksJob = viewModelScope.launch {
            when (_currentFilter.value) {
                TaskFilter.ALL -> repository.allTasks
                TaskFilter.PRIORITY_LOW -> repository.getTasksByPriority(Priority.LOW)
                TaskFilter.PRIORITY_MEDIUM -> repository.getTasksByPriority(Priority.MEDIUM)
                TaskFilter.PRIORITY_HIGH -> repository.getTasksByPriority(Priority.HIGH)
                TaskFilter.PRIORITY_CRITICAL -> repository.getTasksByPriority(Priority.CRITICAL)
                TaskFilter.COMPLETED -> repository.getTasksByCompletion(true)
                TaskFilter.ACTIVE -> repository.getTasksByCompletion(false)
            }.collect { taskList ->
                _tasks.value = taskList.sortedWith(
                    compareBy<Task> { it.isCompleted }
                        .thenBy { it.priority.order }
                        .thenByDescending { it.id }
                )
            }
        }
    }

    fun setFilter(filter: TaskFilter) {
        _currentFilter.value = filter
        loadTasks()
    }

    fun addTask(title: String, priority: Priority = Priority.MEDIUM) {
        viewModelScope.launch {
            val task = Task(id = 0, title = title, priority = priority)
            repository.insertTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}
