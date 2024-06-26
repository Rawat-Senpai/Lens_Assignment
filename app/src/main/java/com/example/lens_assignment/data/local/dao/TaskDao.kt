package com.example.lens_assignment.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lens_assignment.data.local.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("Select * from task ORDER BY todayDate Desc")
    fun getAllTask() : Flow<List<Task>>

    @Query("Select * from task WHERE title LIKE :searchQuery OR description LIKE :searchQuery ")
    fun searchInDatabase(searchQuery:String) : Flow<List<Task>>

    @Insert
    suspend fun insertTask(task:Task)

    @Update
    suspend fun updateTask(task:Task)

    @Delete
    suspend fun  deleteTask(task:Task)


    @Query("SELECT * FROM task WHERE completed = 0 ORDER BY " +
            "CASE WHEN priorityLevel = 'HIGH' THEN 1 " +
            "WHEN priorityLevel = 'MEDIUM' THEN 2 " +
            "WHEN priorityLevel = 'LOW' THEN 3 END")
    fun getIncompleteTasksSortedByPriority(): Flow<List<Task>>

}