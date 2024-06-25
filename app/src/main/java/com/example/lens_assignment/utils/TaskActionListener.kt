package com.example.lens_assignment.utils

import com.example.lens_assignment.data.local.entity.Task

interface TaskActionListener {
    fun onEditTask(task: Task)
    fun onDeleteTask(task: Task)
}