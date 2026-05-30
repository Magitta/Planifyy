package com.example.planifyy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        val dbHelper = DatabaseHelper(this)
        val tvStats = findViewById<TextView>(R.id.tvStats)
        val btnShare = findViewById<Button>(R.id.btnShare)

        val completed = dbHelper.getCompletedTasksCount()
        val total = dbHelper.getTotalTaskCount()

        // Показвам статистиката
        tvStats.text = "$completed от $total задачи"

        // Логика за споделяне
        btnShare.setOnClickListener {
            if (total == 0) {
                Toast.makeText(this, "Нямаш задачи за споделяне!", Toast.LENGTH_SHORT).show()
            } else {
                val shareText = "Виж моя прогрес в Planifyy: Изпълних $completed от $total задачи!"
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(sendIntent, "Сподели прогреса чрез:"))
            }
        }

        // Навигация
        findViewById<Button>(R.id.btnNavHome).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        findViewById<Button>(R.id.btnNavAdd).setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
            finish()
        }
        findViewById<Button>(R.id.btnNavCalendar).setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
            finish()
        }
    }
}