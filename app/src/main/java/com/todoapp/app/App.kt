package com.todoapp.app

import android.app.Application
import com.todoapp.di.AppComponent
import com.todoapp.di.DaggerAppComponent
import com.todoapp.di.DatabaseModule

class App : Application() {

    val appComponent by lazy {
        initialiseAppComponent()
    }

    private fun initialiseAppComponent(): AppComponent {
        val builder = DaggerAppComponent.builder()
        return builder.databaseModule(DatabaseModule(this)).build()
    }
}