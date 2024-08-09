package Faculty_Admin.Announcement

import Faculty_Admin.Dashboards.AdminDashboard
import Faculty_Admin.Dashboards.FacultyDashboard
import Student.Dashboard.StudentDashboard
import Student_Admin_Faculty.ViewCourses.Courses_comboboxesAdmin
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gece_sisapp20.R

class AdminChooseAnnouncement : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_choose_announcement)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()

        val backbtn = findViewById<ImageButton>(R.id.backButton)
        backbtn.setOnClickListener {
                val intent = Intent(this, Admin_AnnouncementStart::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            startActivity(intent)
        }

        val facultyproceedbtn = findViewById<Button>(R.id.facultybutton)
        val studentproceedbtn = findViewById<Button>(R.id.Studentbutton)

        facultyproceedbtn.setOnClickListener{
            val intent = Intent(this, Admin_Announcement2::class.java).apply {// Takes us to faculty announcement
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        studentproceedbtn.setOnClickListener{
            val intent = Intent(this, Admin_Announcement::class.java).apply {// Takes us to students announcement
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Admin_AnnouncementStart::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)

    }


}