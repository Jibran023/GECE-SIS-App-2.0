package Student.Dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import Student_Admin_Faculty.Complaint_Feedback.ComplaintScreenFirst
import Student.Grades.GradesScreenFirst
import Student.ProfileView.ProfilePicture
import com.example.gece_sisapp20.R
import Student.Policies.StudentDashboardPolicies
import Student.Announcement.Student_Announcement
import Student_Admin_Faculty.ViewCourses.courses_comboboxes
import Student.Attendance.student_attendance_comboboxes
import android.util.Log


class StudentDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_dashboard)
        val userType = intent.getStringExtra("USER_TYPE")
        val userID = intent.getStringExtra("USER_ID")

        Log.d("ID-TYPE", "ID Type: $userType and ID: $userID")

        // added user and color check (DONE)
        val attendanceicon = findViewById<LinearLayout>(R.id.attendance_icon)
        attendanceicon.setOnClickListener {
            val intent = Intent(this, student_attendance_comboboxes::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // added user and color check (DONE)
        val policiesicon = findViewById<LinearLayout>(R.id.policies_icon)
        policiesicon.setOnClickListener {
            if (userType == "student")
            {
                val intent = Intent(this, StudentDashboardPolicies::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            else if (userType == "other")
            {
                val intent = Intent(this, StudentDashboardPolicies::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }

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
        val gradesicon = findViewById<LinearLayout>(R.id.gradesicon)
        gradesicon.setOnClickListener {
            val intent = Intent(this, GradesScreenFirst::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // user check added (DONE)
        val coursesicon = findViewById<LinearLayout>(R.id.coursesicon)
        coursesicon.setOnClickListener {
            var intent = Intent(this, courses_comboboxes::class.java).apply {
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