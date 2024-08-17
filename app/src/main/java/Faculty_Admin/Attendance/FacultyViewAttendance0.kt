package Faculty_Admin.Attendance

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.R

class FacultyViewAttendance0 : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID : String

    private lateinit var selectedCohort: String

    private var OfferedCourses: ArrayList<String> = arrayListOf()
    private lateinit var selectedCourse: String
    private lateinit var selectedCourseName: String

    private var SessionID: ArrayList<String> = arrayListOf()
    private lateinit var selectedCourseSessionID: String

    private var OfferedCoursesID: ArrayList<String> = arrayListOf()
    private lateinit var selectedCourseID: String

    private var OfferedSectionID: ArrayList<String> = arrayListOf()
    private lateinit var selectedSecionID: String

    private var OfferedSectionName: ArrayList<String> = arrayListOf()
    private lateinit var selectedSecionName: String
    private var FacultyIDs: ArrayList<String> = arrayListOf()
    private lateinit var selectedFacultyID: String

    private lateinit var cohortSpinner: Spinner
    private lateinit var courseSpinner: Spinner
    private lateinit var sectionSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_view_attendance0)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()

        var pass: Boolean = false
        val backtbtn = findViewById<ImageView>(R.id.backbtn)
        backtbtn.setOnClickListener {
            val intent = Intent(this, FacultyChooseAttendance::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        cohortSpinner = findViewById(R.id.cohort_spinner)
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
//                Toast.makeText(this@FacultyViewAttendance0, "Selected: $selectedCohort", Toast.LENGTH_SHORT).show()

                fetchOfferedCourses()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where no item is selected if needed
                Toast.makeText(this@FacultyViewAttendance0, "No session selected", Toast.LENGTH_SHORT).show()
            }
        }

        courseSpinner = findViewById(R.id.semester_spinner)
        courseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCourse = parent?.getItemAtPosition(position) as String
//                Toast.makeText(this@FacultyViewAttendance0, "Selected: $selectedCourse", Toast.LENGTH_SHORT).show()
                selectedCourseID = OfferedCoursesID[position] // Get the corresponding CourseID using the position
                selectedCourseName = OfferedCourses[position]
                selectedCourseSessionID = SessionID[position]
                if (userType == "admin"){
                    selectedFacultyID = FacultyIDs[position] // saving faculty id for admin use
                }

                pass = true
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where no item is selected if needed
                Toast.makeText(this@FacultyViewAttendance0, "No Course selected", Toast.LENGTH_SHORT).show()
                pass = false
            }
        }

        sectionSpinner = findViewById(R.id.section_spinner)
        sectionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSecionName = parent?.getItemAtPosition(position) as String
//                Toast.makeText(this@FacultyViewAttendance0, "Selected: $selectedSecionName", Toast.LENGTH_SHORT).show()
                selectedSecionID = OfferedSectionID[position] // Get the corresponding SectionID using the position
                pass = true
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where no item is selected if needed
                Toast.makeText(this@FacultyViewAttendance0, "No Course selected", Toast.LENGTH_SHORT).show()
                pass = false
            }
        }

        val proceedButton: Button = findViewById(R.id.nextbtn)
        proceedButton.setOnClickListener {
            if (!pass){
                Toast.makeText(this@FacultyViewAttendance0, "Please select a valid Cohort/Course/Section", Toast.LENGTH_SHORT).show()
            }
            else {
                Log.d("FACDETAILS", "Cohort: $selectedCohort | Course: $selectedCourse | CourseID: $selectedCourseID | Section: $selectedSecionName | SectionID: $selectedSecionName | Session: $selectedCourseSessionID")
                val intent = Intent(this, FacultyViewAttendance::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    putExtra("COHORT", selectedCohort)
                    putExtra("COURSE", selectedCourse)
                    putExtra("COURSEID", selectedCourseID)
                    putExtra("SECTION", selectedSecionName)
                    putExtra("SECTIONID", selectedSecionID)
                    putExtra("SESSIONID", selectedCourseSessionID)
                    if (userType == "admin"){
                        putExtra("SELECTED_FACULTY_ID", selectedFacultyID)
                    }
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, FacultyChooseAttendance::class.java).apply {
            putExtra("USER_TYPE", userType)
            putExtra("USER_ID", userID)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }


    private fun fetchCohorts(callback: (List<String>) -> Unit) {
        if (userType == "faculty"){
            val reqQueue: RequestQueue = Volley.newRequestQueue(this)
            val apiGetCohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Faculty/Attendance/fetch_faculty_courses.php?FacultyID=$userID"
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apiGetCohorts,
                null,
                { response ->
                    val cohorts = mutableListOf<String>()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val cohort = jsonObject.getString("Year")
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
        else if (userType == "admin") {
            val reqQueue: RequestQueue = Volley.newRequestQueue(this)
            val apiGetCohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Faculty/Attendance/fetch_admin_cohorts.php"
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apiGetCohorts,
                null,
                { response ->
                    Log.d("AdminCohorts", "Cohorts: $response")
                    val cohorts = mutableListOf<String>()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val cohort = jsonObject.getString("Year")
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
    }

    private fun fetchOfferedCourses(){
        if (userType == "faculty"){
            val reqQueue: RequestQueue = Volley.newRequestQueue(this)
            val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Faculty/Attendance/fetch_faculty_courses2.php?FacultyID=$userID"

            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apigetcohorts,
                null,
                { response ->
                    Log.d("Fetched Offered Courses", "Fetched Offered Courses are: $response")
                    try {
                        OfferedCourses.clear() // Clearing previous courses
                        OfferedCoursesID.clear()
                        OfferedSectionID.clear()
                        OfferedSectionName.clear()
                        SessionID.clear()
                        for (i in 0 until response.length()) {
                            val jsonObject = response.getJSONObject(i)
                            val courseName = jsonObject.getString("CNAME")
                            val courseID = jsonObject.getString("CourseID")
                            val sessionID = jsonObject.getString("SessionID")
                            val sectionName = jsonObject.getString("SectionName")
                            val sectionID = jsonObject.getString("SectionID")
                            OfferedCourses.add(courseName)
                            OfferedCoursesID.add(courseID)
                            SessionID.add(sessionID)
                            OfferedSectionID.add(sectionID)
                            OfferedSectionName.add(sectionName)
//                        Log.d("FOSC", "Section: $courseName | Course: $selectedCourseName | Instructor: $selectedCourseIDInstructor")
                        }
                        val courseAdapter = ArrayAdapter( // Update the courseSpinner with the fetched courses
                            this,
                            android.R.layout.simple_spinner_item,
                            OfferedCourses
                        )
                        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        courseSpinner.adapter = courseAdapter

                        val sectionAdapter = ArrayAdapter( // Update the secionSpinner with the fetched sections
                            this,
                            android.R.layout.simple_spinner_item,
                            OfferedSectionName
                        )
                        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        sectionSpinner.adapter = sectionAdapter

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
        else if (userType == "admin"){
            val reqQueue: RequestQueue = Volley.newRequestQueue(this)
            val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Faculty/Attendance/fetch_admin_courses.php?FacultyID=$selectedCohort"

            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apigetcohorts,
                null,
                { response ->
                    Log.d("Fetched Offered Courses", "Fetched Offered Courses are: $response")
                    try {
                        OfferedCourses.clear() // Clearing previous courses
                        OfferedCoursesID.clear()
                        OfferedSectionID.clear()
                        OfferedSectionName.clear()
                        FacultyIDs.clear()
                        SessionID.clear()
                        for (i in 0 until response.length()) {
                            val jsonObject = response.getJSONObject(i)
                            val courseName = jsonObject.getString("CNAME")
                            val courseID = jsonObject.getString("CourseID")
                            val sessionID = jsonObject.getString("SessionID")
                            val sectionName = jsonObject.getString("SectionName")
                            val sectionID = jsonObject.getString("SectionID")
                            val facID = jsonObject.getString("FacultyID")
                            OfferedCourses.add(courseName)
                            OfferedCoursesID.add(courseID)
                            SessionID.add(sessionID)
                            OfferedSectionID.add(sectionID)
                            OfferedSectionName.add(sectionName)
                            FacultyIDs.add(facID)
//                        Log.d("FOSC", "Section: $courseName | Course: $selectedCourseName | Instructor: $selectedCourseIDInstructor")
                        }
                        val courseAdapter = ArrayAdapter( // Update the courseSpinner with the fetched courses
                            this,
                            android.R.layout.simple_spinner_item,
                            OfferedCourses
                        )
                        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        courseSpinner.adapter = courseAdapter

                        val sectionAdapter = ArrayAdapter( // Update the secionSpinner with the fetched sections
                            this,
                            android.R.layout.simple_spinner_item,
                            OfferedSectionName
                        )
                        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        sectionSpinner.adapter = sectionAdapter

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



}