package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class courses_comboboxes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_courses_comboboxes)

        var backbtn = findViewById<Button>(R.id.courses_comboboxes_backbutton)
        backbtn.setOnClickListener {
            var intent = Intent(this, StudentDashboard::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        var nextbtn = findViewById<Button>(R.id.nextbutton)
        nextbtn.setOnClickListener {
            var intent = Intent(this, CoursesScreenFirst::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // Find Spinners
        val yearSpinner = findViewById<Spinner>(R.id.year_spinner)
        val semesterSpinner = findViewById<Spinner>(R.id.semester_spinner)


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

    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, StudentDashboard::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}