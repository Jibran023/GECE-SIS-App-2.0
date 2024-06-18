package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class ComplaintTrack : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_complaint_track)

        val track_backbtn = findViewById<ImageButton>(R.id.track_back_button)
        track_backbtn.setOnClickListener {
            val intent = Intent(this, ComplaintScreenFirst::class.java)
            startActivity(intent)
        }
    }
}