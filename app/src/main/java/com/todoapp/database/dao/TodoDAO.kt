package com.todoapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.todoapp.database.entity.Task

@Dao
interface TodoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("DELETE FROM todoTable WHERE id=:taskId")
    suspend fun deleteTask(taskId: String)

    @Query("SELECT * FROM todoTable")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM todoTable WHERE id=:taskId")
    fun getTaskById(taskId: String): LiveData<List<Task>>

}
