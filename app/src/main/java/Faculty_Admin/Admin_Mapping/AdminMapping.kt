package Faculty_Admin.Admin_Mapping

import Faculty_Admin.Dashboards.AdminDashboard
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
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
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AdminMapping : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID: String
    private lateinit var selectedCohorts: List<String>
    private lateinit var selectedCohort: String
    private lateinit var selectedCourse: String
    private var OfferedCourses: ArrayList<String> = arrayListOf()
    private lateinit var selectedCourseName: String
    private lateinit var selectedCourseID: String
    private var OfferedCoursesID: ArrayList<String> = arrayListOf()
    private var OfferedCoursesInstructor: ArrayList<String> = arrayListOf()
    private lateinit var selectedCourseIDInstructor: String
    private var FacultyID: ArrayList<String> = arrayListOf()
    private lateinit var selectedCourseFacultyID: String
    private var SessionID: ArrayList<String> = arrayListOf()
    private lateinit var selectedCourseSessionID: String
    private var DescriptionIDs: ArrayList<String> = arrayListOf()
    private lateinit var selectedCourseDescription: String

    private lateinit var courseSpinner: Spinner
    private lateinit var sectionSpinner: Spinner



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_mapping)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()

        var pass: Boolean = false

        selectedCohorts = emptyList()


        val backbtn = findViewById<ImageButton>(R.id.backButton)
        backbtn.setOnClickListener {
            var intent = Intent(this, AdminDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // Find views by ID
        val cohortSpinner: Spinner = findViewById(R.id.cohort_spinner)
        fetchCohorts { cohort ->
            val yearAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                cohort
            )
            yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            cohortSpinner.adapter = yearAdapter
        }
        cohortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCohort = parent?.getItemAtPosition(position) as String
                Toast.makeText(this@AdminMapping, "Selected: $selectedCohort", Toast.LENGTH_SHORT).show()

                fetchOfferedCourses()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where no item is selected if needed
                Toast.makeText(this@AdminMapping, "No session selected", Toast.LENGTH_SHORT).show()
            }
        }

        courseSpinner = findViewById(R.id.course_spinner)
        courseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCourse = parent?.getItemAtPosition(position) as String
                Toast.makeText(this@AdminMapping, "Selected: $selectedCourse", Toast.LENGTH_SHORT).show()

                // Get the corresponding CourseID using the position
                selectedCourseID = OfferedCoursesID[position]
                selectedCourseIDInstructor = OfferedCoursesInstructor[position]
                selectedCourseName = OfferedCourses[position]
                selectedCourseFacultyID = FacultyID[position]
                selectedCourseSessionID = SessionID[position]
                selectedCourseDescription = DescriptionIDs[position]

                pass = true

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where no item is selected if needed
                Toast.makeText(this@AdminMapping, "No Course selected", Toast.LENGTH_SHORT).show()
                pass = false
            }
        }

        val nextbtn = findViewById<Button>(R.id.nextbutton)
        nextbtn.setOnClickListener {
            if (!pass) {
                // Show a toast message if course ID is empty
                Toast.makeText(this, "Please select a course before proceeding.", Toast.LENGTH_SHORT).show()
            } else {
                var intent = Intent(this, AdminMapping2::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    putExtra("COHORT", selectedCohort)
                    putExtra("COURSE_ID", selectedCourseID)
                    putExtra("INSTRUCTOR", selectedCourseIDInstructor)
                    putExtra("COURSE_NAME", selectedCourseName)
                    putExtra("FACULTY_ID", selectedCourseFacultyID)
                    putExtra("COURSE_SESSION", selectedCourseSessionID)
                    putExtra("COURSE_DESC", selectedCourseDescription)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, AdminDashboard::class.java).apply {
            putExtra("USER_TYPE", userType)
            putExtra("USER_ID", userID)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun fetchCohorts(callback: (List<String>) -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
//        val apiGetCohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Announcements/Admin/fetch_cohorts.php"
        val apiGetCohorts = "${LoginScreen.BASE_URL}/geceapi/fetch_cohortsN.php"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apiGetCohorts,
            null,
            { response ->
                val cohorts = mutableListOf<String>()
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val cohort = jsonObject.getString("cohort")
                    cohorts.add(cohort)
                }
                callback(cohorts)
            },
            { error ->
                Log.e("FetchCohorts", "Error fetching cohorts: ${error.message}")
                callback(emptyList())
            }
        )
        reqQueue.add(jsonArrayRequest)
    }

    private fun fetchOfferedCourses(){
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
//        val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Admin/Mapping/fetch_offered_courses.php?rollNumber=$selectedCohort"

        val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/fetch_offered_coursesN.php?rollNumber=$selectedCohort"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apigetcohorts,
            null,
            { response ->
                Log.d("Fetched Offered Courses", "Fetched Offered Courses are: $response")
                try {
                    OfferedCourses.clear() // Clearing previous courses
                    OfferedCoursesID.clear()
                    OfferedCoursesInstructor.clear()
                    FacultyID.clear()
                    SessionID.clear()
                    DescriptionIDs.clear()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val courseName = jsonObject.getString("Name")
                        val courseID = jsonObject.getString("CourseID")
                        val instructor = jsonObject.getString("FacultyName")
                        val faculty_id = jsonObject.getString("FacultyID")
                        val sessionID = jsonObject.getString("SessionID")
                        val description = jsonObject.getString("Description")
                        OfferedCourses.add(courseName)
                        OfferedCoursesID.add(courseID)
                        OfferedCoursesInstructor.add(instructor)
                        FacultyID.add(faculty_id)
                        SessionID.add(sessionID)
                        DescriptionIDs.add(description)

//                        Log.d("FOSC", "Section: $courseName | Course: $selectedCourseName | Instructor: $selectedCourseIDInstructor")
                    }
                    // Update the courseSpinner with the fetched courses
                    val courseAdapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_item,
                        OfferedCourses
                    )
                    courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    courseSpinner.adapter = courseAdapter

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                Log.e("StudentsDataFetchCourses", "Error fetching the data in FetchCourses: ${error.message}")
            }
        )
        reqQueue.add(jsonArrayRequest)
    }





}