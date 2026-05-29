package com.example.planifyy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Създаваме инстанция на нашия нов DatabaseHelper
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_task)

        // Инициализираме новия помощник за базата
        dbHelper = DatabaseHelper(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSave = findViewById<Button>(R.id.btnSave)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etTime = findViewById<EditText>(R.id.etTime)

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val time = etTime.text.toString()

            if (title.isNotEmpty() && time.isNotEmpty()) {
                // Извикваме нашия метод за добавяне
                val id = dbHelper.addTask(title, time)

                if (id != -1L) {
                    Toast.makeText(this, "Успешно записано!", Toast.LENGTH_SHORT).show()
                    etTitle.text.clear()
                    etTime.text.clear()

                    // ЧЕТЕНЕ НА ДАННИТЕ:
                    // След запис веднага извличаме какво има в базата
                    val allTasks = dbHelper.getAllTasks()
                    Toast.makeText(this, "Всички задачи:\n$allTasks", Toast.LENGTH_LONG).show()

                } else {
                    Toast.makeText(this, "Грешка при запис!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Попълни всички полета!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}