package Student_Admin_Faculty.ViewCourses

import Faculty_Admin.Dashboards.AdminDashboard
import Faculty_Admin.Dashboards.FacultyDashboard
import Student.Dashboard.StudentDashboard
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

class AdminChooseFacultyStudent : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID: String
    private var whotosearch: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_choose_faculty_student)

        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()

        val backbtn = findViewById<ImageButton>(R.id.backButton)
        backbtn.setOnClickListener {
            if (userType == "faculty") {
                val intent = Intent(this, FacultyDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            } else if (userType == "admin") {
                val intent = Intent(this, AdminDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            else if (userType == "student") {
                val intent = Intent(this, StudentDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        }

        val facultyproceedbtn = findViewById<Button>(R.id.facultybutton)
        val studentproceedbtn = findViewById<Button>(R.id.Studentbutton)

        facultyproceedbtn.setOnClickListener{
            val intent = Intent(this, Courses_comboboxesAdmin::class.java).apply {
                whotosearch = "faculty"
                putExtra("WHO_TO_SEARCH", whotosearch)
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        studentproceedbtn.setOnClickListener{
            val intent = Intent(this, Courses_comboboxesAdmin::class.java).apply {
                whotosearch = "student"
                putExtra("WHO_TO_SEARCH", whotosearch)
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (userType == "faculty") {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FacultyDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        } else if (userType == "admin") {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AdminDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
        else if (userType == "student") {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, StudentDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
        finish()
    }

}