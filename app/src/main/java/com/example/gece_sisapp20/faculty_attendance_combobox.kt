package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class faculty_attendance_combobox : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_attendance_combobox)

        val backbtn = findViewById<ImageView>(R.id.faculty_backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, FacultyDashboard::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        val nextbtn = findViewById<Button>(R.id.nextbtn)
        nextbtn.setOnClickListener {
            val intent = Intent(this, faculty_attendance_page2::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }


        val cohort_spinner = findViewById<Spinner>(R.id.cohort_spinner)
        val semester_spinner = findViewById<Spinner>(R.id.semester_spinner)




    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, FacultyDashboard::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}