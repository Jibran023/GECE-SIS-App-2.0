package Faculty_Admin.Attendance

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gece_sisapp20.R

class faculty_markattendance2 : AppCompatActivity() {
    private var userType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_markattendance2)
        userType = intent.getStringExtra("USER_TYPE")

        val backbtn = findViewById<ImageView>(R.id.backbtn)
        backbtn.setOnClickListener {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FacultyChooseAttendance::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // Dummy data. Uses AttendanceCourse from item_CourseAdapter.kt where each course has Name, Section, List of class dates
        val courses = listOf(
            AttendanceCourse("Mathematics", listOf("Section A", "Section B"), listOf("2024-09-07", "2024-07-09", "2024-07-06")),
            AttendanceCourse("Physics", listOf("Section C", "Section D"), listOf("2024-07-02", "2024-07-09", "2024-07-03")),
            AttendanceCourse("Chemistry", listOf("Section A", "Section C"), listOf("2024-07-08", "2024-07-02", "2024-07-04")),
            AttendanceCourse("Biology", listOf("Section A", "Section D"), listOf("2024-07-01", "2024-07-08", "2024-07-06")),
            AttendanceCourse("History", listOf("Section B", "Section C"), listOf("2024-07-02", "2024-07-09", "2024-07-03")),
            AttendanceCourse("Geography", listOf("Section B", "Section D"), listOf("2024-07-08", "2024-07-02", "2024-07-04")),
            AttendanceCourse("English", listOf("Section A", "Section C"), listOf("2024-07-01", "2024-07-08", "2024-07-06")),
            AttendanceCourse("Art", listOf("Section A", "Section D"), listOf("2024-07-02", "2024-07-09", "2024-07-03")),
            AttendanceCourse("Computer Science", listOf("Section D", "Section B"), listOf("2024-07-08", "2024-07-02", "2024-07-04")),
            AttendanceCourse("Economics", listOf("Section D", "Section C"), listOf("2024-07-01", "2024-07-08", "2024-07-06"))
        )

        // Initialize RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AttendanceCourseAdapter(courses) { course -> onCourseClick(course) }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, FacultyChooseAttendance::class.java).apply {
            putExtra("USER_TYPE", userType)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }


    private fun onCourseClick(course: AttendanceCourse) {
        val intent = Intent(this, faculty_markattendance1::class.java).apply {
            putExtra("USER_TYPE", userType)
            putExtra("course_name", course.name)
            putStringArrayListExtra("sections", ArrayList(course.sections))
            putStringArrayListExtra("class_dates", ArrayList(course.classDates)) // Passing the Name, Section, Dates to next screen
        }
        startActivity(intent)
    }

}