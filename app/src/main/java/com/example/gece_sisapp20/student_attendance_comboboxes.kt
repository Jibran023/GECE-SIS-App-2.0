package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class student_attendance_comboboxes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_attendance_comboboxes)

        val back_btn = findViewById<ImageView>(R.id.attendance_combo_boxes_firstpage_backbtn)
        back_btn.setOnClickListener{
            val intent = Intent(this, StudentDashboard::class.java)
            startActivity(intent)
        }

        // Find Spinners
        val yearSpinner = findViewById<Spinner>(R.id.year_spinner)
        val semesterSpinner = findViewById<Spinner>(R.id.semester_spinner)
        val courseSpinner = findViewById<Spinner>(R.id.course_spinner)

        // Setup ArrayAdapter for Year
        val yearAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.year_array,
            android.R.layout.simple_spinner_item
        )
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = yearAdapter

        // Setup ArrayAdapter for Semester
        val semesterAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.semester_array,
            android.R.layout.simple_spinner_item
        )
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        semesterSpinner.adapter = semesterAdapter

        // Setup ArrayAdapter for Course
        val courseAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.course_array,
            android.R.layout.simple_spinner_item
        )
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        courseSpinner.adapter = courseAdapter
    }
}