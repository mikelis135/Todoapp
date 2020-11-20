package com.todoapp.di

import com.todoapp.ui.edit.EditFragment
import com.todoapp.ui.home.HomeAdapter
import com.todoapp.ui.home.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])

interface AppComponent {
    fun inject(homeFragment: HomeFragment)
    fun inject(editFragment: EditFragment)
    fun inject(homeAdapter: HomeAdapter)
}