package com.todoapp.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoapp.database.dao.TodoDAO
import com.todoapp.database.entity.Task
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditViewModel @Inject constructor(
    private val todoDAO: TodoDAO
) : ViewModel() {

    fun insertTask(task: Task) {
        viewModelScope.launch {
            todoDAO.insertTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            todoDAO.updateTask(task)
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            todoDAO.deleteTask(taskId)
        }

    }

}