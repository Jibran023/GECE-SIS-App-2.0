package com.example.gece_sisapp20

import android.content.Intent
import androidx.activity.enableEdgeToEdge
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


data class Course(val courseName: String, val courseId: String,  val internalMarks: String, val externalMarks: String,val grade: String, val gpa: String)

class GradesScreenSecond : AppCompatActivity() {
    private var userType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_grades_screen_second)
        userType = intent.getStringExtra("USER_TYPE")

        val backbtn = findViewById<ImageView>(R.id.gradesscrnsecond_backbtn)
        backbtn.setOnClickListener{
            val intent = Intent(this, GradesScreenFirst::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Sample data
        val courses = listOf(
            Course("Mathematics", "MATH101", "90", "60", "A", "3.6"),
            Course("Physics",  "PHY101", "90", "60", "80", "3.6"),
            Course("Chemistry", "CHEM101", "90", "60", "80", "3.6"),
            Course("Biology",  "BIO101", "90", "60", "80", "3.6"),
            Course("Computer Science", "CS101", "90", "60", "80", "3.6")
        )

        val adapter = CourseAdapter(this, courses)
        recyclerView.adapter = adapter

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, GradesScreenFirst::class.java).apply {
            putExtra("USER_TYPE", userType)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}