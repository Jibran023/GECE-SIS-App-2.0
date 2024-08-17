package Student.Attendance

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
import android.widget.ImageView
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


class student_attendance_comboboxes : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID: String
    private lateinit var rollno: String
    private lateinit var selectedSessionDescription: String
    private lateinit var selectedCourse: String
    private lateinit var courseIDList: ArrayList<String>
    private lateinit var selectedCourseID: String
    private var coursesList: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_attendance_comboboxes)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()

        val back_btn = findViewById<ImageView>(R.id.attendance_combo_boxes_firstpage_backbtn)
        back_btn.setOnClickListener{
            if (userType == "faculty") {
                val intent = Intent(this, FacultyDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            } else if (userType == "admin") {
                val intent = Intent(this, AdminDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            else if (userType == "student") {
                val intent = Intent(this, StudentDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        }

        val next_btn = findViewById<Button>(R.id.nextbutton)
        next_btn.setOnClickListener {
            val intent = Intent(this, attendance_page2::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                putExtra("SELECTED_COURSE_ID", selectedCourseID)
                putExtra("SELECTED_SESSION", selectedSessionDescription)
                putExtra("ROLL_NO", rollno)

                Log.d("JAYGAY", "CourseID: $selectedCourseID | Course Name: $selectedCourse | Session: $selectedSessionDescription")
                // Pass the selected course ID
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // Initialize courseIDList
        courseIDList = arrayListOf()

        // Find Spinners

        val courseSpinner = findViewById<Spinner>(R.id.course_spinner)

        // Setup ArrayAdapter for Year
        fetchstudentrollenumber()

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
                Toast.makeText(this@student_attendance_comboboxes, "Selected: $selectedSessionDescription", Toast.LENGTH_SHORT).show()

                fetchcourses()

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where no item is selected if needed
                Toast.makeText(this@student_attendance_comboboxes, "No session selected", Toast.LENGTH_SHORT).show()
            }
        }

        // Setup ArrayAdapter for Course
        val courseAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.course_array,
            android.R.layout.simple_spinner_item
        )
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        courseSpinner.adapter = courseAdapter

        courseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCourse = parent?.getItemAtPosition(position) as String
                Toast.makeText(this@student_attendance_comboboxes, "Selected: $selectedCourse", Toast.LENGTH_SHORT).show()
                // Assuming you have a way to fetch or get the course ID
                // You might need to modify this if you have a separate method or API call to get the course ID

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@student_attendance_comboboxes, "No course selected", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (userType == "faculty") {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FacultyDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        } else if (userType == "admin") {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AdminDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
        else if (userType == "student") {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, StudentDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
        finish()
    }

    private fun fetchSessionDescriptions(callback: (List<String>) -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
//        val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Student/Courses/fetchsessions.php"
        val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/fetchsessionsN.php"
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
//        val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Student/Courses/studentsrole.php?id=$studentIDint"
        val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/studentsroleN.php?id=$studentIDint"

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


        if (userType == "student") {
            val rollnoint = rollno?.toIntOrNull() ?: 1
//            val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Student/Courses/fetchcoursesstudent.php?rollNumber=$rollnoint&semesterDescription=$selectedSessionDescription"
            val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/fetchcoursesstudentN.php?rollNumber=$rollnoint&semesterDescription=$selectedSessionDescription"

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
                            val courseID = jsonObject.getString("CourseID")
                            coursesList.add(courseName)
                            courseIDList.add(courseID) // Store course ID
                        }
                        updateCourseSpinner(coursesList)
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

    private fun updateCourseSpinner(courses: List<String>) {
        val courseSpinner = findViewById<Spinner>(R.id.course_spinner)
        val courseAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            courses
        )
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        courseSpinner.adapter = courseAdapter

        courseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCourse = parent?.getItemAtPosition(position) as String
                selectedCourseID = courseIDList[position] // Get the corresponding course ID
                Toast.makeText(this@student_attendance_comboboxes, "Selected Course: $selectedCourse, ID: $selectedCourseID", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@student_attendance_comboboxes, "No course selected", Toast.LENGTH_SHORT).show()
            }
        }
    }




}