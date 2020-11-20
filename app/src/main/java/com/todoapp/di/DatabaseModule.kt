package com.todoapp.di

import android.content.Context
import androidx.room.Room
import com.todoapp.app.AppConstants
import com.todoapp.database.AppDatabase
import com.todoapp.database.dao.TodoDAO
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(private val context: Context) {

    @Singleton
    @Provides
    fun providesTodoDAO(appDatabase: AppDatabase): TodoDAO {
        return appDatabase.todoDAO()
    }

    @Singleton
    @Provides
    fun providesDatabase(): AppDatabase {

        var appInstance: AppDatabase? = null

        val tempInstance = appInstance
        if (tempInstance != null) {
            return tempInstance
        }

        synchronized(this) {
            val instance = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                AppConstants.DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
            appInstance = instance
            return instance
        }

    }

}