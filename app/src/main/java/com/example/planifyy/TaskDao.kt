package com.example.planifyy

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {
    @Query("SELECT * FROM table_tasks")
    fun getAllTasks(): List<TaskEntity>

    @Insert
    fun insertTask(task: TaskEntity)

    @Delete
    fun deleteTask(task: TaskEntity)
}