package com.example.lens_assignment.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lens_assignment.data.local.dao.TaskDao
import com.example.lens_assignment.data.local.entity.Task

@Database(entities = [Task::class], version =1 )
abstract class TaskDatabase : RoomDatabase () {
    abstract fun taskDao(): TaskDao

}