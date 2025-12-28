package com.example.to_do_list.data.repository

import com.example.to_do_list.data.TaskDao
import com.example.to_do_list.data.toEntity
import com.example.to_do_list.data.toTask
import com.example.to_do_list.data.models.Priority
import com.example.to_do_list.data.models.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks().map { entities ->
        entities.map { it.toTask() }
    }

    fun getTasksByPriority(priority: Priority): Flow<List<Task>> {
        return taskDao.getTasksByPriority(priority.name).map { entities ->
            entities.map { it.toTask() }
        }
    }

    fun getTasksByCompletion(isCompleted: Boolean): Flow<List<Task>> {
        return taskDao.getTasksByCompletion(isCompleted).map { entities ->
            entities.map { it.toTask() }
        }
    }

    suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(task.toEntity())
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }
}
