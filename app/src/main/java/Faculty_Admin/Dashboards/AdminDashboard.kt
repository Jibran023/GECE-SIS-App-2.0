package Faculty_Admin.Dashboards

import Faculty_Admin.Admin_Mapping.AdminMapping
import Faculty_Admin.Announcement.Faculty_Announcement2
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import Student_Admin_Faculty.Complaint_Feedback.ComplaintScreenFirst
import Faculty_Admin.Attendance.FacultyChooseAttendance
import Faculty_Admin.Policies.FacultyDashboardPolicies
import Faculty_Admin.Grading.Faculty_markGrades
import Faculty_Admin.ProfileView.Profile_Faculty
import Student_Admin_Faculty.ViewCourses.AdminChooseFacultyStudent
import com.example.gece_sisapp20.R
import Student_Admin_Faculty.ViewCourses.courses_comboboxes
import android.util.Log


class AdminDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_dashboard)

        val userType = intent.getStringExtra("USER_TYPE")
        val userID = intent.getStringExtra("USER_ID")

        Log.d("AdminDasboard_Data", "User Type is: $userType | User ID is: $userID")

        // added user check (DONE)
        val attendanceicon = findViewById<LinearLayout>(R.id.attendance_icon)
        attendanceicon.setOnClickListener {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FacultyChooseAttendance::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // added user check (DONE)
        val policiesicon = findViewById<LinearLayout>(R.id.policies_icon)
        policiesicon.setOnClickListener {
            val intent = Intent(this, FacultyDashboardPolicies::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // added user check
        val complain_and_feedback = findViewById<LinearLayout>(R.id.complaintfeedbackicon)
        complain_and_feedback.setOnClickListener {
            var intent = Intent(this, ComplaintScreenFirst::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // added user check (DONE)
        val announcementicon = findViewById<LinearLayout>(R.id.announcementicon)
        announcementicon.setOnClickListener {
            val intent = Intent(this, Faculty_Announcement2::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // added user check (DONE)
        val gradesicon = findViewById<LinearLayout>(R.id.gradesicon)
        gradesicon.setOnClickListener {
            val intent = Intent(this, Faculty_markGrades::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // added user check (DONE)
        val coursesicon = findViewById<LinearLayout>(R.id.coursesicon)
        coursesicon.setOnClickListener {
            var intent = Intent(this, AdminChooseFacultyStudent::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // added user check (DONE)
        val profile_pic = findViewById<ImageView>(R.id.studentdashboardprofilepicture)
        profile_pic.setOnClickListener {
            var intent = Intent(this, Profile_Faculty::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // no need to add user check (DONE)
        val mappingicon = findViewById<LinearLayout>(R.id.mappingicon)
        mappingicon.setOnClickListener {
            var intent = Intent(this, AdminMapping::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }
}