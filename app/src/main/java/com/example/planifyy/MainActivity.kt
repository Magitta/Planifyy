package com.example.planifyy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // Инициализация на базата данни
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "task-database"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ВНИМАНИЕ: Промени това на R.layout.activity_add_task, за да тестваш бутона "Запази"
        setContentView(R.layout.activity_add_task)

        // Инициализация на изгледа (ако имаш ID 'main' в XML-а)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Логика за бутона "Запази"
        val btnSave = findViewById<Button>(R.id.btnSave)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etTime = findViewById<EditText>(R.id.etTime)

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val time = etTime.text.toString()

            if (title.isNotEmpty() && time.isNotEmpty()) {
                val newTask = TaskEntity(title = title, time = time, date = "Днес")

                // Запис в базата данни чрез корутина
                lifecycleScope.launch(Dispatchers.IO) {
                    db.taskDao().insertTask(newTask)

                    launch(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Успешно записано!", Toast.LENGTH_SHORT).show()
                        etTitle.text.clear()
                        etTime.text.clear()
                    }
                }
            } else {
                Toast.makeText(this, "Попълни всички полета!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}