package com.example.planifyy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmptyState: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.rvTasks)
        tvEmptyState = findViewById(R.id.tvEmptyState)
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnNavAdd).setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        findViewById<Button>(R.id.btnNavCalendar).setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
        }

        loadTasks()
    }

    private fun loadTasks() {
        val calendar = Calendar.getInstance()
        val today = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"

        val taskList = dbHelper.getTasksForDate(today)

        if (taskList.isEmpty()) {
            tvEmptyState.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            tvEmptyState.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        val adapterData = taskList.map { Pair(it.first, it.second) }
        val adapter = TaskAdapter(adapterData) { position: Int ->
            dbHelper.deleteTask(taskList[position].first)
            loadTasks()
        }
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }
}