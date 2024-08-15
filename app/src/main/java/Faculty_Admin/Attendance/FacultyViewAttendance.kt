package Faculty_Admin.Attendance

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.R


class FacultyViewAttendance : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID : String

    private lateinit var selectedCohort: String
    private lateinit var selectedCourse: String
    private lateinit var selectedCourseSessionID: String
    private lateinit var selectedCourseID: String
    private lateinit var selectedSecionID: String
    private lateinit var selectedSecionName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_view_attendance)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()
        selectedCohort = intent.getStringExtra("COHORT").toString()
        selectedCourse = intent.getStringExtra("COURSE").toString()
        selectedCourseID = intent.getStringExtra("COURSEID").toString()
        selectedSecionName = intent.getStringExtra("SECTION").toString()
        selectedSecionID = intent.getStringExtra("SECTIONID").toString()
        selectedCourseSessionID = intent.getStringExtra("SESSIONID").toString()

        val back_btn = findViewById<ImageButton>(R.id.backButton)
        back_btn.setOnClickListener {
            val intent = Intent(this, FacultyViewAttendance0::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                putExtra("COHORT", selectedCohort)
                putExtra("COURSE", selectedCourse)
                putExtra("COURSEID", selectedCourseID)
                putExtra("SECTION", selectedSecionName)
                putExtra("SECTIONID", selectedSecionID)
                putExtra("SESSIONID", selectedCourseSessionID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        fetchClassDates { dates ->
            val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = DateAdapter(this, dates) { date ->
                // Handle date click
                val intent = Intent(this, FacultyViewAttendance2::class.java).apply {
                    putExtra("SELECTED_DATE", date)
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    putExtra("COHORT", selectedCohort)
                    putExtra("COURSE", selectedCourse)
                    putExtra("COURSEID", selectedCourseID)
                    putExtra("SECTION", selectedSecionName)
                    putExtra("SECTIONID", selectedSecionID)
                    putExtra("SESSIONID", selectedCourseSessionID)
                }
                startActivity(intent)
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, FacultyViewAttendance0::class.java).apply {
            putExtra("USER_TYPE", userType)
            putExtra("USER_ID", userID)
            putExtra("COHORT", selectedCohort)
            putExtra("COURSE", selectedCourse)
            putExtra("COURSEID", selectedCourseID)
            putExtra("SECTION", selectedSecionName)
            putExtra("SECTIONID", selectedSecionID)
            putExtra("SESSIONID", selectedCourseSessionID)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun fetchClassDates(callback: (List<String>) -> Unit) {
        if (userType == "faculty" || userType == "admin"){
            val reqQueue: RequestQueue = Volley.newRequestQueue(this)
            val apiGetCohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Faculty/Attendance/fetch_section_attendance_dates.php?FacultyID=$userID&SectionID=$selectedSecionID"
            Log.d("Fetched Dates", "URL: $apiGetCohorts")
            Log.d("Fetched Dates", "Section Name: $selectedSecionID | SectionID: $selectedSecionName")
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apiGetCohorts,
                null,
                { response ->
                    Log.d("Fetched Dates", "Response: $response")
                    val dates = mutableListOf<String>()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val date = jsonObject.getString("Date")
                        dates.add(date)
                    }
                    callback(dates)

                },
                { error ->
                    Log.e("FetchCohorts", "Error fetching cohorts: ${error.message}")
                    callback(emptyList())
                }
            )
            reqQueue.add(jsonArrayRequest)
        }
        else if (userType == "user") {

        }
    }

}