package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class ComplaintScreenFirst : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_complaint_screen_first)

        var complaintfirstpagebackbtn = findViewById<ImageView>(R.id.complaintfirstpageback_button)
        complaintfirstpagebackbtn.setOnClickListener {
            var intent = Intent(this, StudentDashboard::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        var filecomplaint = findViewById<RelativeLayout>(R.id.filecomplaint_relativelayoutid)
        filecomplaint.setOnClickListener{
            var intent = Intent(this, ComplaintScreenFileComplaint::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        var fileComplaintanon = findViewById<RelativeLayout>(R.id.filecomplainanon)
        fileComplaintanon.setOnClickListener{
            var intent = Intent(this, ComplaintScreenAnonComplain::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        var suggestions = findViewById<RelativeLayout>(R.id.suggestions_icon)
        suggestions.setOnClickListener {
            var intent = Intent(this, ComplaintScreenSuggestions::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        var trackreqs = findViewById<RelativeLayout>(R.id.trackreqs)
        trackreqs.setOnClickListener {
            val intent = Intent(this, ComplaintTrack::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, StudentDashboard::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}