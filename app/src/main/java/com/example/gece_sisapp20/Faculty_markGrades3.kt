package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class StudentGrade(val name: String, val percentage: String)

class Faculty_markGrades3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_mark_grades3)

        val backbtn = findViewById<ImageView>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, Faculty_markGrades2::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // Dummy data
        val students = listOf(
            StudentGrade("Alice", "95%"),
            StudentGrade("Bob", "88%"),
            StudentGrade("Charlie", "78%"),
            StudentGrade("Diana", "92%"),
            StudentGrade("Eve", "85%"),
            StudentGrade("Frank", "80%")
        )

        // Setup RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StudentGradeAdapter(students)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Faculty_markGrades2::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}