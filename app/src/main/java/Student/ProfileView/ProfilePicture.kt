package Student.ProfileView

import Faculty_Admin.Dashboards.AdminDashboard
import Faculty_Admin.Dashboards.FacultyDashboard
import Student.Dashboard.StudentDashboard
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.R


class ProfilePicture : AppCompatActivity() {
    private var userType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_picture)
        userType = intent.getStringExtra("USER_TYPE")

        val dashboard_icon = findViewById<LinearLayout>(R.id.dashboard_icon)
        val inbox_icon = findViewById<LinearLayout>(R.id.inbox_icon)
        val logout_icon = findViewById<LinearLayout>(R.id.logout_id)

        dashboard_icon.setOnClickListener {
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

        logout_icon.setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java).apply {
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