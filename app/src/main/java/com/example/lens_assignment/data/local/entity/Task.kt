package com.example.lens_assignment.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.IDN

@Entity(tableName = "task")
data class Task (
    @PrimaryKey (autoGenerate = true)val id:Int =0,
    val title:String?= null,
    val description:String?= null,
    val dueDate:String?=null,
    val priorityLevel:String?=null,
    val date:Long
){
}