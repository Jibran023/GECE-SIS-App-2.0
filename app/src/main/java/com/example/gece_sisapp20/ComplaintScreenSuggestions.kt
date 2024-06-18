package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class ComplaintScreenSuggestions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_complaint_screen_suggestions)

        val suggestions_backbtn = findViewById<ImageButton>(R.id.Suggestions_backbutton)
        suggestions_backbtn.setOnClickListener {
            val intent = Intent(this, ComplaintScreenFirst::class.java)
            startActivity(intent)
        }
    }
}