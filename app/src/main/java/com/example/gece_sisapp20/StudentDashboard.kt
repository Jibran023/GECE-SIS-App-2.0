package com.example.gece_sisapp20

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
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
        val attendanceimgview = findViewById<ImageView>(R.id.attendance_imageview)
        val attendancetext = findViewById<TextView>(R.id.attendance_text)

        val announcementicon = findViewById<LinearLayout>(R.id.announcementicon)
        val announcementimgview = findViewById<ImageView>(R.id.announcementimgview)
        val announcementtext = findViewById<TextView>(R.id.announcementtext)

        val gradesicon = findViewById<LinearLayout>(R.id.gradesicon)
        val gradesimgview = findViewById<ImageView>(R.id.gradesimageview)
        val gradestext = findViewById<TextView>(R.id.gradestext)

        val policiesicon = findViewById<LinearLayout>(R.id.policies_icon)
        val policiesimgview = findViewById<ImageView>(R.id.policiesimgview)
        val policiestext = findViewById<TextView>(R.id.policiestext)

        val coursesicon = findViewById<LinearLayout>(R.id.coursesicon)
        val coursesimgview = findViewById<ImageView>(R.id.coursesimgview)
        val coursestext = findViewById<TextView>(R.id.coursestext)

    }
}