package com.todoapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoapp.database.dao.TodoDAO
import com.todoapp.database.entity.Task
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val todoDAO: TodoDAO
) : ViewModel() {

    var todoLiveData: LiveData<List<Task>> = MutableLiveData()
    var emptyTaskLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        getAllTasks()
    }

    private fun getAllTasks() {
        viewModelScope.launch {
            todoLiveData = todoDAO.getAllTasks()
        }
    }

    fun checkEmpty(taskList: List<Task>) {
        emptyTaskLiveData.value = taskList.isNullOrEmpty()
    }

}