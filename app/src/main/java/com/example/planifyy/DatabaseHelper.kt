package com.example.planifyy

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "Planify.db", null, 3) {

    override fun onCreate(db: SQLiteDatabase) {
        // Добавихме isDone (0 = неизпълнено, 1 = изпълнено)
        db.execSQL("CREATE TABLE tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, time TEXT, date TEXT, isDone INTEGER DEFAULT 0)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tasks")
        onCreate(db)
    }

    fun addTask(title: String, time: String, date: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("time", time)
            put("date", date)
            put("isDone", 0)
        }
        return db.insert("tasks", null, values)
    }

    fun updateTaskStatus(title: String, isDone: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("isDone", isDone)
        }
        db.update("tasks", values, "title = ?", arrayOf(title))
    }

    fun deleteTask(title: String): Boolean {
        val db = this.writableDatabase
        return db.delete("tasks", "title = ?", arrayOf(title)) > 0
    }

    // Връща 4 елемента: title, time, date, isDone
    fun getTasksForDate(date: String): List<Array<Any>> {
        val list = mutableListOf<Array<Any>>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tasks WHERE date = ? ORDER BY CAST(substr(time, 1, instr(time, ':') - 1) AS INTEGER) ASC, CAST(substr(time, instr(time, ':') + 1) AS INTEGER) ASC", arrayOf(date))

        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val time = cursor.getString(cursor.getColumnIndexOrThrow("time"))
                val taskDate = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val isDone = cursor.getInt(cursor.getColumnIndexOrThrow("isDone"))
                list.add(arrayOf(title, time, taskDate, isDone))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getTotalTaskCount(): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM tasks", null)
        var count = 0
        if (cursor.moveToFirst()) count = cursor.getInt(0)
        cursor.close()
        return count
    }

    fun getCompletedTasksCount(): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM tasks WHERE isDone = 1", null)
        var count = 0
        if (cursor.moveToFirst()) count = cursor.getInt(0)
        cursor.close()
        return count
    }
}