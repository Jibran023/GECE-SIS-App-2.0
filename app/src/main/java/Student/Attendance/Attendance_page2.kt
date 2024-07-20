package Student.Attendance

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gece_sisapp20.R


class attendance_page2 : AppCompatActivity() {
    private var userType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_page2)
        userType = intent.getStringExtra("USER_TYPE")

        val backbtn = findViewById<ImageButton>(R.id.back_button)
        backbtn.setOnClickListener {
            val intent = Intent(this, student_attendance_comboboxes::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }


        val attendanceRecyclerView = findViewById<RecyclerView>(R.id.attendance_recycler_view)
        // Sample data
        val attendanceList = listOf(
            AttendanceRecord("2024-06-01", "Present"),
            AttendanceRecord("2024-06-02", "Absent"),
            AttendanceRecord("2024-06-03", "Late"),
            AttendanceRecord("2024-06-04", "Present"),
            // Add more records as needed
        )

        // Setting up the adapter
        attendanceRecyclerView.layoutManager = LinearLayoutManager(this)
        attendanceRecyclerView.adapter = AttendanceAdapter(attendanceList)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, student_attendance_comboboxes::class.java).apply {
            putExtra("USER_TYPE", userType)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}