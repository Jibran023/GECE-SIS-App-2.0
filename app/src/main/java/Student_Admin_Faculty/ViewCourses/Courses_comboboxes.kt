package Student_Admin_Faculty.ViewCourses

import Faculty_Admin.Dashboards.AdminDashboard
import Faculty_Admin.Dashboards.FacultyDashboard
import Student.Dashboard.StudentDashboard
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.gece_sisapp20.R


class courses_comboboxes : AppCompatActivity() {
    private var userType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_courses_comboboxes)
        userType = intent.getStringExtra("USER_TYPE")

        var backbtn = findViewById<Button>(R.id.courses_comboboxes_backbutton)
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

        var nextbtn = findViewById<Button>(R.id.nextbutton)
        nextbtn.setOnClickListener {
            var intent = Intent(this, CoursesScreenFirst::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // Find Spinners
        val yearSpinner = findViewById<Spinner>(R.id.year_spinner)
        val semesterSpinner = findViewById<Spinner>(R.id.semester_spinner)


        // Setup ArrayAdapter for Year
        val yearAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.year_array,
            android.R.layout.simple_spinner_item
        )
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = yearAdapter

        // Setup ArrayAdapter for Semester
        val semesterAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.semester_array,
            android.R.layout.simple_spinner_item
        )
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        semesterSpinner.adapter = semesterAdapter

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