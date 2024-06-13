package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StudentDashboardPolicies : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_dashboard_policies)

        val policies_backbtn = findViewById<ImageButton>(R.id.policiesback_button)
        policies_backbtn.setOnClickListener {
            val intent = Intent(this, StudentDashboard::class.java)
            startActivity(intent)
        }
    }
}