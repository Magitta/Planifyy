package com.example.planifyy

import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class) // Това казва на теста да симулира Android среда
class DatabaseTest {

    @Test
    fun testDatabaseOperations() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val dbHelper = DatabaseHelper(context)

        val id = dbHelper.addTask("Тест за шестица", "09:00", "2026-06-01")

        assertTrue("Задачата не беше добавена успешно", id > 0)

        val count = dbHelper.getTotalTaskCount()
        assertTrue("Броячът на задачи не работи", count >= 1)
    }
}