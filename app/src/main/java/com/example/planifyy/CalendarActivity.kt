package com.example.planifyy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CalendarActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvTaskCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.rvCalendarTasks)
        tvTaskCount = findViewById(R.id.tvTaskCount)
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnNavHome).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        findViewById<Button>(R.id.btnNavAdd).setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
            finish()
        }
        findViewById<Button>(R.id.btnNavStats).setOnClickListener {
            startActivity(Intent(this, StatsActivity::class.java))
            finish()
        }

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            updateTaskList(selectedDate)
        }
    }

    private fun updateTaskList(selectedDate: String) {
        val taskList = dbHelper.getTasksForDate(selectedDate)

        tvTaskCount.text = if (taskList.isEmpty()) {
            "Нямаш задачи за $selectedDate"
        } else {
            "Планирани задачи: ${taskList.size}"
        }

        // Подаваме taskList директно, тъй като TaskAdapter вече е настроен за него
        val adapter = TaskAdapter(taskList) { position: Int ->
            // Изтриваме по заглавие, което е на индекс 0
            dbHelper.deleteTask(taskList[position][0].toString())
            updateTaskList(selectedDate)
        }
        recyclerView.adapter = adapter
    }
}