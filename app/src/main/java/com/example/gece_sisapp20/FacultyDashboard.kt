package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class FacultyDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_dashboard)

        val attendanceicon = findViewById<LinearLayout>(R.id.attendance_icon)
        attendanceicon.setOnClickListener {
            val intent = Intent(this, faculty_attendance_combobox::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        val policiesicon = findViewById<LinearLayout>(R.id.policies_icon)
//        policiesicon.setOnClickListener {
//            val intent = Intent(this, StudentDashboardPolicies::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            }
//            startActivity(intent)
//        }

        val complain_and_feedback = findViewById<LinearLayout>(R.id.complaintfeedbackicon)
//        complain_and_feedback.setOnClickListener {
//            var intent = Intent(this, ComplaintScreenFirst::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            }
//            startActivity(intent)
//        }

        val announcementicon = findViewById<LinearLayout>(R.id.announcementicon)
//        announcementicon.setOnClickListener {
//            val intent = Intent(this, Student_Announcement::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            }
//            startActivity(intent)
//        }

        val gradesicon = findViewById<LinearLayout>(R.id.gradesicon)
//        gradesicon.setOnClickListener {
//            val intent = Intent(this, GradesScreenFirst::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            }
//            startActivity(intent)
//        }

        val coursesicon = findViewById<LinearLayout>(R.id.coursesicon)
//        coursesicon.setOnClickListener {
//            var intent = Intent(this, courses_comboboxes::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            }
//            startActivity(intent)
//        }

        val profile_pic = findViewById<ImageView>(R.id.studentdashboardprofilepicture)
//        profile_pic.setOnClickListener {
//            var intent = Intent(this, ProfilePicture::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            }
//            startActivity(intent)
//        }
    }
}