package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class faculty_markattendance1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_markattendance1)

        val backtbtn = findViewById<ImageView>(R.id.backbtn)
//        val nextbtn = findViewById<Button>(R.id.nextbtn)

        backtbtn.setOnClickListener {
            val intent = Intent(this, FacultyDashboard::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }



        // Initialize the Spinner
        val sectionSpinner: Spinner = findViewById(R.id.section_spinner)

        // Dummy sections data
        val sections = arrayOf("Section A", "Section B", "Section C", "Section D")

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sections)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        sectionSpinner.adapter = adapter

        // Handle button click
        val proceedButton: Button = findViewById(R.id.nextbtn)
        proceedButton.setOnClickListener {
            val selectedSection = sectionSpinner.selectedItem.toString()
            // Show selected section as a Toast message
            Toast.makeText(this, "Selected Section: $selectedSection", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, faculty_markattendance2::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)

            // Proceed with marking attendance logic
            // You can start a new activity or fragment here
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, FacultyDashboard::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}