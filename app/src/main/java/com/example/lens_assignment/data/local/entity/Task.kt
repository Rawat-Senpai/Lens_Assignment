package com.example.lens_assignment.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task (
    @PrimaryKey (autoGenerate = true)val id:Int =0,
    val title:String?= null,
    val description:String?= null,
    val dueDate:Long?=null,
    val priorityLevel:String?=null,
    val todayDate:Long,
    val completed:Boolean=false,
    val location:String?= null
)