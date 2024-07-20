package Faculty_Admin.Grading

import Faculty_Admin.Attendance.AttendanceCourse
import Faculty_Admin.Attendance.AttendanceCourseAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gece_sisapp20.R


class Faculty_markGrades2 : AppCompatActivity() {
    private var userType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_mark_grades2)
        userType = intent.getStringExtra("USER_TYPE")

        val backbtn = findViewById<ImageView>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, Faculty_markGrades::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // Dummy data
        val courses = listOf(
            AttendanceCourse("Mathematics", listOf("Section A", "Section B"), listOf("2024-01-07", "2024-07-08", "2024-07-06")),
            AttendanceCourse("Physics", listOf("Section A", "Section B"), listOf("2024-07-02", "2024-07-09", "2024-07-03")),
            AttendanceCourse("Chemistry", listOf("Section A", "Section B"), listOf("2024-07-08", "2024-07-02", "2024-07-04")),
            AttendanceCourse("Biology", listOf("Section A", "Section B"), listOf("2024-07-01", "2024-07-08", "2024-07-06")),
            AttendanceCourse("History", listOf("Section A", "Section B"), listOf("2024-07-02", "2024-07-09", "2024-07-03")),
            AttendanceCourse("Geography", listOf("Section A", "Section B"), listOf("2024-07-08", "2024-07-02", "2024-07-04")),
            AttendanceCourse("English", listOf("Section A", "Section B"), listOf("2024-07-01", "2024-07-08", "2024-07-06")),
            AttendanceCourse("Art", listOf("Section A", "Section B"), listOf("2024-07-02", "2024-07-09", "2024-07-03")),
            AttendanceCourse("Computer Science", listOf("Section A", "Section B"), listOf("2024-07-08", "2024-07-02", "2024-07-04")),
            AttendanceCourse("Economics", listOf("Section A", "Section B"), listOf("2024-07-01", "2024-07-08", "2024-07-06"))
        )

        // Initialize RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AttendanceCourseAdapter(courses){ course -> onCourseClick(course) }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Faculty_markGrades::class.java).apply {
            putExtra("USER_TYPE", userType)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun onCourseClick(course: AttendanceCourse) {
        val intent = Intent(this, Faculty_markGrades3::class.java).apply {
            putStringArrayListExtra("sections", ArrayList(course.sections))
            putExtra("course_name", course.name)
            putExtra("USER_TYPE", userType)
        }
        startActivity(intent)
    }

}