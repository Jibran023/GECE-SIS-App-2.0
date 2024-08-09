package com.example.gece_sisapp20

import Student.Announcement.Student_Announcement
import Student.Attendance.student_attendance_comboboxes
import Student.Grades.GradesScreenFirst
import Student.Policies.StudentDashboardPolicies
import Student.ProfileView.ProfilePicture
import Student_Admin_Faculty.Complaint_Feedback.ComplaintScreenFirst
import Student_Admin_Faculty.ViewCourses.courses_comboboxes
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class OtherDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_other_dashboard)
        val userType = intent.getStringExtra("USER_TYPE")
        val userID = intent.getStringExtra("USER_ID")

        Log.d("ID-TYPE", "ID Type: $userType and ID: $userID")

        // added user and color check (DONE)
        val policiesicon = findViewById<LinearLayout>(R.id.policies_icon)
        policiesicon.setOnClickListener {
            val intent = Intent(this, StudentDashboardPolicies::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // user check and color check
        val complain_and_feedback = findViewById<LinearLayout>(R.id.complaintfeedbackicon)
        complain_and_feedback.setOnClickListener {
            var intent = Intent(this, ComplaintScreenFirst::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // added user and color check (DONE)
        val announcementicon = findViewById<LinearLayout>(R.id.announcementicon)
        announcementicon.setOnClickListener {
            val intent = Intent(this, Student_Announcement::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // added user check (DONE)
        val profile_pic = findViewById<ImageView>(R.id.studentdashboardprofilepicture)
        profile_pic.setOnClickListener {
            var intent = Intent(this, ProfilePicture::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }
}