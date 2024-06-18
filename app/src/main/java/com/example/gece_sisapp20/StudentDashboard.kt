package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text


class StudentDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_dashboard)

        val studentprofilepic = findViewById<ImageView>(R.id.studentdashboardprofilepicture)

        val attendanceicon = findViewById<LinearLayout>(R.id.attendance_icon)
        attendanceicon.setOnClickListener {
            val intent = Intent(this, student_attendance_comboboxes::class.java)
            startActivity(intent)
        }

        val policiesicon = findViewById<LinearLayout>(R.id.policies_icon)
        policiesicon.setOnClickListener {
            val intent = Intent(this, StudentDashboardPolicies::class.java)
            startActivity(intent)
        }

        val complain_and_feedback = findViewById<LinearLayout>(R.id.complaintfeedbackicon)
        complain_and_feedback.setOnClickListener {
            var intent = Intent(this, ComplaintScreenFirst::class.java)
            startActivity(intent)
        }

        val announcementicon = findViewById<LinearLayout>(R.id.announcementicon)

        val gradesicon = findViewById<LinearLayout>(R.id.gradesicon)
        gradesicon.setOnClickListener {
            val intent = Intent(this, GradesScreenFirst::class.java)
            startActivity(intent)
        }

        val coursesicon = findViewById<LinearLayout>(R.id.coursesicon)
        coursesicon.setOnClickListener {
            var intent = Intent(this, CoursesScreenFirst::class.java)
            startActivity(intent)
        }




    }
}