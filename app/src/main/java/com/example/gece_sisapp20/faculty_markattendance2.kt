package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class faculty_markattendance2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_markattendance2)

        val backbtn = findViewById<ImageView>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, faculty_markattendance1::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // Dummy data
        val courses = listOf(
            AttendanceCourse("Mathematics", "101"),
            AttendanceCourse("Physics", "102"),
            AttendanceCourse("Chemistry", "103"),
            AttendanceCourse("Biology", "104"),
            AttendanceCourse("History", "201"),
            AttendanceCourse("Geography", "202"),
            AttendanceCourse("English", "301"),
            AttendanceCourse("Art", "302"),
            AttendanceCourse("Computer Science", "401"),
            AttendanceCourse("Economics", "402")
        )

        // Initialize RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AttendanceCourseAdapter(courses)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, faculty_markattendance1::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}