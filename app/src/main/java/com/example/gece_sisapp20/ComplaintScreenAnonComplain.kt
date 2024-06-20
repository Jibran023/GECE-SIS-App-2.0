package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class ComplaintScreenAnonComplain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_complaint_screen_anon_complain)

        var anoncomplain_backbtn = findViewById<ImageView>(R.id.Anoncomplainback_button)
        anoncomplain_backbtn.setOnClickListener {
            var intent = Intent(this, ComplaintScreenFirst::class.java)
            startActivity(intent)
        }
    }
}