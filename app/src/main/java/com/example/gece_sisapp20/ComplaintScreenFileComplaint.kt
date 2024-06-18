package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class ComplaintScreenFileComplaint : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_complaint_screen_file_complaint)

        var complaintform_backbtn = findViewById<ImageButton>(R.id.complaintform_backbutton)
        complaintform_backbtn.setOnClickListener {
            var intent = Intent(this, ComplaintScreenFirst::class.java)
            startActivity(intent)
        }
    }
}