package com.todoapp.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.todoapp.app.AppConstants.Companion.TODO_TABLE
import kotlinx.android.parcel.Parcelize

@Entity(tableName = TODO_TABLE)
@Parcelize
class Task(

    @PrimaryKey
    val id: String,

    val content: String,

    val url: String,

    val image: String

) : Parcelable

