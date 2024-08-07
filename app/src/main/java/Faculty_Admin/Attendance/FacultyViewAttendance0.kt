package Faculty_Admin.Attendance

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gece_sisapp20.R

class FacultyViewAttendance0 : AppCompatActivity() {
    private var userType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_view_attendance0)
        userType = intent.getStringExtra("USER_TYPE")


        val backtbtn = findViewById<ImageView>(R.id.backbtn)
        backtbtn.setOnClickListener {
            val intent = Intent(this, FacultyChooseAttendance::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        val cohortSpinner: Spinner = findViewById(R.id.cohort_spinner)
        val semesterSpinner: Spinner = findViewById(R.id.semester_spinner)
        val sectionSpinner: Spinner = findViewById(R.id.section_spinner)

        val proceedButton: Button = findViewById(R.id.nextbtn)
        proceedButton.setOnClickListener {
            val intent = Intent(this, FacultyViewAttendance::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)


        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, FacultyChooseAttendance::class.java).apply {
            putExtra("USER_TYPE", userType)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}