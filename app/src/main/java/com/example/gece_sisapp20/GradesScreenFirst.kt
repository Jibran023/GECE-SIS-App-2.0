package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class GradesScreenFirst : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_grades_screen_first)

        val nextbtn = findViewById<Button>(R.id.gradesscrnfirst_nextbtn)
        nextbtn.setOnClickListener {
            val intent = Intent(this, GradesScreenSecond::class.java)
            startActivity(intent)
        }

        val backbtn = findViewById<Button>(R.id.Grades_backbutton)
        backbtn.setOnClickListener {
            val intent = Intent(this, StudentDashboard::class.java)
            startActivity(intent)
        }
    }
}