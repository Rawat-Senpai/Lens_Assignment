package com.example.lens_assignment.ui.homeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.lens_assignment.data.local.dao.TaskDao
import com.example.lens_assignment.data.local.entity.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel  @Inject constructor(private val taskDao: TaskDao): ViewModel() {

    val task = taskDao.getAllTask()
    val taskChannel = Channel<TaskEvents>()
    val taskEvents = taskChannel.receiveAsFlow()

    fun insertTask(task:Task) = viewModelScope.launch {
        taskDao.insertTask(task)
        taskChannel.send(TaskEvents.NavigateToTaskFragments)

    }

    fun updateTask(task:Task) = viewModelScope.launch {
        taskDao.updateTask(task)
        taskChannel.send(TaskEvents.NavigateToTaskFragments)
    }

    fun deleteTask(task:Task) = viewModelScope.launch {
        taskDao.deleteTask(task)
        taskChannel.send(TaskEvents.ShowUndoSnackBar("Task Deleted Successfully",task))
    }

    fun searchDatabase(searchQuery:String):LiveData<List<Task>>{
        return taskDao.searchInDatabase(searchQuery = searchQuery).asLiveData()
    }


    sealed class TaskEvents{
        data class ShowUndoSnackBar(val msg:String,val task:Task):TaskEvents()

        object NavigateToTaskFragments:TaskEvents()
    }
}