package Faculty_Admin.Attendance

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gece_sisapp20.R


class FacultyViewAttendance : AppCompatActivity() {
    private var userType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_view_attendance)
        userType = intent.getStringExtra("USER_TYPE")

        val back_btn = findViewById<ImageButton>(R.id.backButton)
        back_btn.setOnClickListener {
            val intent = Intent(this, FacultyViewAttendance0::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // Dummy data for dates
        val dates = listOf(
            "2024-01-10",
            "2024-01-12",
            "2024-01-14",
            "2024-01-16",
            "2024-01-18",
            "2024-01-20"
        )

        // Setup RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = DateAdapter(dates)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, FacultyViewAttendance0::class.java).apply {
            putExtra("USER_TYPE", userType)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}