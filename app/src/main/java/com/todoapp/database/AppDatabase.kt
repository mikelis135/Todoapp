package com.todoapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.todoapp.database.dao.TodoDAO
import com.todoapp.database.entity.Task

@Database(entities = [Task::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDAO(): TodoDAO
}