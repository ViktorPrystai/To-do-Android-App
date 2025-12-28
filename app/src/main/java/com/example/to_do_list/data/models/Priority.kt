package com.example.to_do_list.data.models

enum class Priority(val displayName: String, val order: Int) {
    LOW("Low", 3),
    MEDIUM("Medium", 2),
    HIGH("High", 1),
    CRITICAL("Critical", 0);

    companion object {
        fun fromString(value: String): Priority {
            return entries.find { it.name == value } ?: MEDIUM
        }
    }
}
