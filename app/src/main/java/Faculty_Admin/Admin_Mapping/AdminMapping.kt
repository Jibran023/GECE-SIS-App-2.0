package Faculty_Admin.Admin_Mapping

import Faculty_Admin.Dashboards.AdminDashboard
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gece_sisapp20.R


class AdminMapping : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_mapping)


        val backbtn = findViewById<ImageButton>(R.id.backButton)
        backbtn.setOnClickListener {
            var intent = Intent(this, AdminDashboard::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        val savebtn = findViewById<Button>(R.id.nextbutton)
        savebtn.setOnClickListener {
            var intent = Intent(this, AdminMapping2::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // Find views by ID
        val cohortSpinner: Spinner = findViewById(R.id.cohort_spinner)
        val semesterSpinner: Spinner = findViewById(R.id.semester_spinner)
        val recyclerViewCourses: RecyclerView = findViewById(R.id.recyclerViewCourses)

        // Setup cohort spinner
        val cohorts = listOf("2022", "2023", "2024")
        val cohortAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cohorts)
        cohortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cohortSpinner.adapter = cohortAdapter

        // Setup semester spinner
        val semesters = listOf("Spring", "Fall")
        val semesterAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, semesters)
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        semesterSpinner.adapter = semesterAdapter

        // Dummy data for courses
        val courses = listOf("Mathematics", "Physics", "Chemistry", "Biology")

        // Setup RecyclerView
        recyclerViewCourses.layoutManager = LinearLayoutManager(this)
//        recyclerViewCourses.adapter = CourseAdapter(courses)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, AdminDashboard::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}