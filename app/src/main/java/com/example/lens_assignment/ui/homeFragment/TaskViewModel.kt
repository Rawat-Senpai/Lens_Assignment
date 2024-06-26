package com.example.lens_assignment.ui.homeFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.lens_assignment.data.local.dao.TaskDao
import com.example.lens_assignment.data.local.entity.Task
import com.example.lens_assignment.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {

    val task = taskDao.getAllTask()
    val taskChannel = Channel<TaskEvents>()
    val taskEvents = taskChannel.receiveAsFlow()

    private val _highPriorityCount = MutableLiveData<Int>()
    val highPriorityCount: LiveData<Int> get() = _highPriorityCount

    private val _mediumPriorityCount = MutableLiveData<Int>()
    val mediumPriorityCount: LiveData<Int> get() = _mediumPriorityCount

    private val _lowPriorityCount = MutableLiveData<Int>()
    val lowPriorityCount: LiveData<Int> get() = _lowPriorityCount

    private val _completedTaskCount = MutableLiveData<Int>()
    val completedTaskCount :LiveData<Int> get() = _completedTaskCount

    private val _inCompletedTaskCount = MutableLiveData<Int>()
    val inCompletedTaskCount :LiveData<Int> = _inCompletedTaskCount


    init {
        calculatePriorityCounts()
        calculateCompletedTask()
    }

    private fun calculateCompletedTask() = viewModelScope.launch {
       val tasks = taskDao.getAllTask().first()

        Log.d("total array list",task.toString())

        val completed = tasks.count {it.completed}
        val pending = tasks.count { !it.completed }

        _completedTaskCount.postValue(completed)
        _inCompletedTaskCount.postValue(pending)


    }

    private fun calculatePriorityCounts() = viewModelScope.launch {
        val tasks = taskDao.getAllTask().first() // Assuming getAllTask returns a Flow<List<Task>>
        val highCount = tasks.count { it.priorityLevel == Constants.HIGH }
        val mediumCount = tasks.count { it.priorityLevel == Constants.MEDIUM }
        val lowCount = tasks.count { it.priorityLevel == Constants.LOW }

        _highPriorityCount.postValue(highCount)
        _mediumPriorityCount.postValue(mediumCount)
        _lowPriorityCount.postValue(lowCount)
    }


    fun insertTask(task: Task) = viewModelScope.launch {
        taskDao.insertTask(task)
        taskChannel.send(TaskEvents.NavigateToTaskFragments)

    }

    fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.updateTask(task)
        taskChannel.send(TaskEvents.NavigateToTaskFragments)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        taskDao.deleteTask(task)
        taskChannel.send(TaskEvents.ShowUndoSnackBar("Task Deleted Successfully", task))
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Task>> {
        return taskDao.searchInDatabase(searchQuery = searchQuery).asLiveData()
    }


    sealed class TaskEvents {
        data class ShowUndoSnackBar(val msg: String, val task: Task) : TaskEvents()

        object NavigateToTaskFragments : TaskEvents()
    }
}