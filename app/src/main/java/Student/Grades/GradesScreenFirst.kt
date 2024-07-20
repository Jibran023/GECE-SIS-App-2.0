package Student.Grades

import Faculty_Admin.Dashboards.AdminDashboard
import Faculty_Admin.Dashboards.FacultyDashboard
import Student.Dashboard.StudentDashboard
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.gece_sisapp20.R


class GradesScreenFirst : AppCompatActivity() {
    private var userType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_grades_screen_first)
        userType = intent.getStringExtra("USER_TYPE")

        val nextbtn = findViewById<Button>(R.id.gradesscrnfirst_nextbtn)
        nextbtn.setOnClickListener {
            val intent = Intent(this, GradesScreenSecond::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        val backbtn = findViewById<Button>(R.id.Grades_backbutton)
        backbtn.setOnClickListener {
            if (userType == "faculty") {
                val intent = Intent(this, FacultyDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            } else if (userType == "admin") {
                val intent = Intent(this, AdminDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            else if (userType == "student") {
                val intent = Intent(this, StudentDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (userType == "faculty") {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FacultyDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        } else if (userType == "admin") {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AdminDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
        else if (userType == "student") {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, StudentDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
        finish()
    }
}