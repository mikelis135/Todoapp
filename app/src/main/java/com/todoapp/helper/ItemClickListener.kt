package com.todoapp.helper

import android.view.View

interface ItemClickListener {

    fun <T> onItemClick(item: T, position: Int)

    fun onItemLongClick(v: View, position: Int)

}
