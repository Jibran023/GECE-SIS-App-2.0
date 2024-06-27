package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar


class faculty_markattendance3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_markattendance3)

        val backbtn = findViewById<ImageView>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, faculty_markattendance2::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // Dummy student data
        val students = listOf(
            Student("Alice", "present"),
            Student("Bob", "absent"),
            Student("Charlie", "late"),
            Student("Diana", "present"),
            Student("Eve", "absent"),
            Student("Frank", "late")
        )

        // Setup RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StudentAdapter(students)

        // DatePicker setup
        val datePicker: DatePicker = findViewById(R.id.date_picker)
        val calendar = Calendar.getInstance()

        datePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))
        { _, year, monthOfYear, dayOfMonth ->
            // Handle date change | You can use the selected date for attendance marking
            val selectedDate = "$dayOfMonth-${monthOfYear + 1}-$year"
            // Do something with the selected date
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, faculty_markattendance2::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}