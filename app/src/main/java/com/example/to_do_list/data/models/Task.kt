package com.example.to_do_list.data.models

data class Task(
    val id: Int,
    val title: String,
    var description: String = "",
    var isCompleted: Boolean = false,
    var priority: Priority = Priority.MEDIUM
)
