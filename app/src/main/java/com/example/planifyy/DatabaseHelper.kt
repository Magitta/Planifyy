package com.example.planifyy

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "Planify.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, time TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tasks")
        onCreate(db)
    }

    // ТАЗИ ФУНКЦИЯ ЛИПСВАШЕ - добави я ето тук:
    fun addTask(title: String, time: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("time", time)
        }
        return db.insert("tasks", null, values)
    }

    fun getAllTasks(): String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tasks", null)
        val tasks = StringBuilder()

        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val time = cursor.getString(cursor.getColumnIndexOrThrow("time"))
                tasks.append("$title ($time)\n")
            } while (cursor.moveToNext())
        }
        cursor.close()
        return tasks.toString()
    }
}