package Student.Attendance

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.R


class attendance_page2 : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID: String
    private lateinit var selectedSessionDescription: String
    private lateinit var selectedCourseID: String
    private lateinit var rollno: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_page2)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()
        selectedSessionDescription = intent.getStringExtra("SELECTED_SESSION").toString()
        selectedCourseID = intent.getStringExtra("SELECTED_COURSE_ID").toString()
        rollno = intent.getStringExtra("ROLL_NO").toString()

//        Toast.makeText(this@attendance_page2, "Selected: $selectedSessionDescription | CourseID: $selectedCourseID", Toast.LENGTH_SHORT).show()

        val backbtn = findViewById<ImageButton>(R.id.back_button)
        backbtn.setOnClickListener {
            val intent = Intent(this, student_attendance_comboboxes::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }


        fetchAttendance()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, student_attendance_comboboxes::class.java).apply {
            putExtra("USER_TYPE", userType)
            putExtra("USER_ID", userID)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun fetchAttendance() {
        if (::selectedCourseID.isInitialized.not() || ::selectedSessionDescription.isInitialized.not() || ::rollno.isInitialized.not()) {
            return
        }

        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apigetAttendance = "http://192.168.18.55/geceapi/Student/Attendance/fetch_attendance.php?rollNumber=$rollno&semesterDescription=$selectedSessionDescription&courseID=$selectedCourseID"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apigetAttendance,
            null,
            { response ->
                Log.d("AttendanceData", "Fetched Attendance Data: $response")
                try {
                    val attendanceList = mutableListOf<AttendanceRecord>()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val date = jsonObject.getString("Date")
                        val status = jsonObject.getString("AttendanceStatus")
                        Log.d("AttendanceData", "Fetched Attendance Data: $date")
                        Log.d("AttendanceData", "Fetched Attendance Data: $status")
                        attendanceList.add(AttendanceRecord(date, status))
                    }
                    updateAttendanceRecyclerView(attendanceList)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                Log.e("AttendanceData", "Error fetching attendance data: ${error.message}")
            }
        )
        reqQueue.add(jsonArrayRequest)
    }

    private fun updateAttendanceRecyclerView(attendanceList: List<AttendanceRecord>) {
        Log.d("AttendanceData", "Updating RecyclerView with ${attendanceList.size} items")
        val attendanceRecyclerView = findViewById<RecyclerView>(R.id.attendance_recycler_view)
        attendanceRecyclerView.layoutManager = LinearLayoutManager(this)
        attendanceRecyclerView.adapter = AttendanceAdapter(attendanceList)
    }

}