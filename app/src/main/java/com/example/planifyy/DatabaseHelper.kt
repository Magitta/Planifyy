package com.example.planifyy

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "Planify.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, time TEXT, date TEXT)")
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
        }
        return db.insert("tasks", null, values)
    }

    fun deleteTask(title: String): Boolean {
        val db = this.writableDatabase
        return db.delete("tasks", "title = ?", arrayOf(title)) > 0
    }

    // Тук добавихме ORDER BY time ASC за автоматично сортиране
    fun getTasksForDate(date: String): List<Triple<String, String, String>> {
        val list = mutableListOf<Triple<String, String, String>>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tasks WHERE date = ? ORDER BY time ASC", arrayOf(date))

        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val time = cursor.getString(cursor.getColumnIndexOrThrow("time"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                list.add(Triple(title, time, date))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
}