package com.todoapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.todoapp.R
import com.todoapp.helper.Tools
import com.todoapp.ui.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        Tools.gotoFragmentOnly(this, HomeFragment())
    }
}