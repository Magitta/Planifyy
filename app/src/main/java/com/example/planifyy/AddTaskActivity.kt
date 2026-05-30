package com.example.planifyy

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        dbHelper = DatabaseHelper(this)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnSelectDate = findViewById<Button>(R.id.btnSelectDate)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etTime = findViewById<EditText>(R.id.etTime)

        btnSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                selectedDate = "$day/${month + 1}/$year"
                btnSelectDate.text = "Дата: $selectedDate"
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        findViewById<Button>(R.id.btnNavHome).setOnClickListener { finish() }
        findViewById<Button>(R.id.btnNavCalendar).setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
            finish()
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val time = etTime.text.toString()

            // Валидация за формат на часа (напр. 09:00)
            val timeRegex = Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")

            if (title.isNotEmpty() && time.isNotEmpty() && selectedDate.isNotEmpty()) {
                if (timeRegex.matches(time)) {
                    if (dbHelper.addTask(title, time, selectedDate) != -1L) {
                        Toast.makeText(this, "Успешно записано!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Грешка при запис!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Моля, въведи час във формат ЧЧ:ММ (напр. 09:00)", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Попълни всички полета и избери дата!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}