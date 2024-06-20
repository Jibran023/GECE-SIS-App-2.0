package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class courses_comboboxes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_courses_comboboxes)

        var backbtn = findViewById<Button>(R.id.courses_comboboxes_backbutton)
        backbtn.setOnClickListener {
            var intent = Intent(this, StudentDashboard::class.java)
            startActivity(intent)
        }

        var nextbtn = findViewById<Button>(R.id.nextbutton)
        nextbtn.setOnClickListener {
            var intent = Intent(this, CoursesScreenFirst::class.java)
            startActivity(intent)
        }
    }
}