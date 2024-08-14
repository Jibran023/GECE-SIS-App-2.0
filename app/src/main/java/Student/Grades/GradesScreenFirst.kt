package Student.Grades

import Faculty_Admin.Dashboards.AdminDashboard
import Faculty_Admin.Dashboards.FacultyDashboard
import Student.Dashboard.StudentDashboard
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.R


class GradesScreenFirst : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var selectedSessionDescription: String
    private lateinit var userID: String
    private var rollno: String? = null
    private var coursesList: ArrayList<String> = arrayListOf() // Variable to store fetched courses

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_grades_screen_first)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()

        val nextbtn = findViewById<Button>(R.id.gradesscrnfirst_nextbtn)
        nextbtn.setOnClickListener {

            // Get the selected position from the Spinner
            val courseSpinner = findViewById<Spinner>(R.id.course_spinner)
            val selectedPosition = courseSpinner.selectedItemPosition

            // Assuming 'coursesList' is a list of course IDs corresponding to the Spinner items
            val selectedCourseID = if (selectedPosition >= 0) coursesList[selectedPosition] else null

            Log.d("SelectedCourse", "Selected course: ${coursesList[selectedPosition]} with ID: $selectedCourseID")
            if (selectedCourseID != "" && rollno != null) {
                val intent = Intent(this, GradesScreenSecond::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    putExtra("ROLL_NUMBER", rollno)  // Pass the roll number
                    putExtra("COURSE_ID", selectedCourseID)  // Pass the course ID
                    putExtra("SESSION", selectedSessionDescription)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            } else {
                // Handle the case where selectedCourseID or rollno is null (show a message, etc.)
                Toast.makeText(this, "Please select a course and ensure Roll Number is available.", Toast.LENGTH_SHORT).show()
            }
        }

        val backbtn = findViewById<Button>(R.id.Grades_backbutton)
        backbtn.setOnClickListener {
            if (userType == "faculty") {
                val intent = Intent(this, FacultyDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            } else if (userType == "admin") {
                val intent = Intent(this, AdminDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            else if (userType == "student") {
                val intent = Intent(this, StudentDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        }

        val yearSpinner = findViewById<Spinner>(R.id.year_spinner)
        fetchSessionDescriptions { sessionDescriptions ->
            val yearAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                sessionDescriptions
            )
            yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            yearSpinner.adapter = yearAdapter
        }

        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSessionDescription = parent?.getItemAtPosition(position) as String
                Toast.makeText(this@GradesScreenFirst, "Selected: $selectedSessionDescription", Toast.LENGTH_SHORT).show()

                // Fetch the courses corresponding to the selected session
                fetchcourses()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where no item is selected if needed
                Toast.makeText(this@GradesScreenFirst, "No session selected", Toast.LENGTH_SHORT).show()
            }
        }

        fetchstudentrollenumber()

    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (userType == "faculty") {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FacultyDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        } else if (userType == "admin") {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AdminDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
        else if (userType == "student") {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, StudentDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
        finish()
    }


    private fun fetchSessionDescriptions(callback: (List<String>) -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Student/Courses/fetchsessions.php"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apigetcohorts,
            null,
            { response ->
                Log.d("SessionData", "Fetched JSON Data: $response")
                try {
                    val sessionDescriptions = mutableListOf<String>()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val description = jsonObject.getString("Description")
                        sessionDescriptions.add(description)
                    }
                    callback(sessionDescriptions)
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback(emptyList()) // In case of error, return an empty list
                }
            },
            { error ->
                Log.e("SessionData", "Error fetching the data: ${error.message}")
                callback(emptyList()) // In case of error, return an empty list
            }
        )
        reqQueue.add(jsonArrayRequest)
    }

    private fun fetchstudentrollenumber(){
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val studentIDint = userID.toIntOrNull()?: 1
        val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Student/Courses/studentsrole.php?id=$studentIDint"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apigetcohorts,
            null,
            { response ->
                Log.d("StudentsData", "Fetched JSON Data: $response")
                try {
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val rolenumber = jsonObject.getString("RollNumber") // Assuming 'id' is a string

                        rollno = rolenumber
                        Log.d("SelectedRollno", "Roll Number is: $rollno")
                        Log.d("Selecteddescription", "Selected academic session is: $selectedSessionDescription")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                Log.e("StudentsData", "Error fetching the data in FetchRollno: ${error.message}")
            }
        )
        reqQueue.add(jsonArrayRequest)
    }

    private fun fetchcourses(){
        if (rollno == null || ::selectedSessionDescription.isInitialized.not()) {
            // Ensure that rollno and selectedSessionDescription are initialized
            return
        }
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val studentIDint = userID.toIntOrNull()?: 1
        val CourseSpinner = findViewById<Spinner>(R.id.course_spinner)

        if (userType == "student") {
            val rollnoint = rollno?.toIntOrNull() ?: 1
            val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Student/Courses/fetchcoursesstudent.php?rollNumber=$rollnoint&semesterDescription=$selectedSessionDescription"

            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apigetcohorts,
                null,
                { response ->
                    Log.d("Students Fetched Courses", "Fetched Courses are: $response")
                    try {
                        coursesList.clear() // Clearing previous courses
                        for (i in 0 until response.length()) {
                            val jsonObject = response.getJSONObject(i)
                            val courseName = jsonObject.getString("Name")
                            coursesList.add(courseName)
                        }

                        // Populate the Spinner with fetched courses
                        val courseAdapter = ArrayAdapter(
                            this,
                            android.R.layout.simple_spinner_item,
                            coursesList
                        )
                        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        CourseSpinner.adapter = courseAdapter

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