package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.ListView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class StudentDashboardPolicies : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_dashboard_policies)

        val listView = findViewById<ListView>(R.id.listView)

        // Sample data
        val policyRecords = listOf(
            PolicyRecord(Policy("Policy 1 Title")),
            PolicyRecord(Policy("Policy 2 Title")),
            PolicyRecord(Policy("Policy 3 Title"))
        )


        val adapter = PolicyAdapter(this, policyRecords)
        listView.adapter = adapter

        val policies_backbtn = findViewById<ImageView>(R.id.policiesback_button)
        policies_backbtn.setOnClickListener {
            val intent = Intent(this, StudentDashboard::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }

}