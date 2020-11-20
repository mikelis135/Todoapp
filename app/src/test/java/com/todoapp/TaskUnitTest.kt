package com.todoapp

import com.todoapp.database.entity.Task
import org.junit.Assert.assertEquals
import org.junit.Test

class TaskUnitTest {

    @Test
    fun task_withCorrectInput() {

        val taskId = "1234"
        val taskContent = "1234"

        val task = Task(taskId, taskContent, "url", "imagepath")

        assertEquals(task.id, "1234")
        assertEquals(task.content, taskContent)
    }

    @Test
    fun task_withInCorrectInput() {

        val taskId = "1234"
        val taskContent = "the content"

        val task = Task(taskId, taskContent, "url", "imagepath")

        //make sure task id is unique and retains value, change id of Task class to 'var' and use assertNotEquals to test this
//        task.id = "12345"

        assertEquals(task.id, taskId)
//        assertNotEquals(task.id, taskId)
    }

}