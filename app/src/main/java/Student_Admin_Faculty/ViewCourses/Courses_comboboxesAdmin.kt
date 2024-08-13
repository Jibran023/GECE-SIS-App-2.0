package Student_Admin_Faculty.ViewCourses

import Faculty_Admin.Dashboards.AdminDashboard
import Faculty_Admin.Dashboards.FacultyDashboard
import Student.Dashboard.StudentDashboard
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
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
import com.example.gece_sisapp20.UserCredentials

class Courses_comboboxesAdmin : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID: String
    private var whotosearch: String? = null
    private lateinit var selectedSessionDescription: String
    private lateinit var selectedpeople: String
    private var coursesList: ArrayList<String> = arrayListOf() // Variable to store fetched courses
    private lateinit var studentorfaculty: TextView

    private var isSessionSelected = false
    private var isPersonSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_courses_comboboxes_admin)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()
        whotosearch = intent.getStringExtra("WHO_TO_SEARCH")


        var backbtn = findViewById<Button>(R.id.courses_comboboxes_backbutton)
        backbtn.setOnClickListener {
                val intent = Intent(this, AdminChooseFacultyStudent::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }

        // Find and Setup ArrayAdapter for Year
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
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener { // Listen for selection events on the year spinner
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSessionDescription = parent?.getItemAtPosition(position) as String
                Toast.makeText(this@Courses_comboboxesAdmin, "Selected: $selectedSessionDescription", Toast.LENGTH_SHORT).show()
                isSessionSelected = true
                checkEnableNextButton()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where no item is selected if needed
                Toast.makeText(this@Courses_comboboxesAdmin, "No session selected", Toast.LENGTH_SHORT).show()
                isSessionSelected = false
                checkEnableNextButton()
            }
        }


        val autocompleteView = findViewById<AutoCompleteTextView>(R.id.faculty_students_autocomplete)
        fetchstudentsdata { allpeople ->
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                allpeople
            )
            autocompleteView.setAdapter(adapter)
        }
        autocompleteView.setOnItemClickListener { parent, view, position, id ->
            selectedpeople = parent.getItemAtPosition(position) as String
            Toast.makeText(this@Courses_comboboxesAdmin, "Selected Student/Faculty: $selectedpeople", Toast.LENGTH_SHORT).show()
            isPersonSelected = true
            checkEnableNextButton()
        }

        val nextbtn = findViewById<Button>(R.id.nextbutton)
        nextbtn.isEnabled = false // Initially disable the button


        nextbtn.setOnClickListener {
            if (isSessionSelected && isPersonSelected) {
                fetchcourses {
                    Log.d("YELLO", "Courses fetched, navigating to next screen.")
                    navigateToCoursesScreen()
                }
            } else {
                Toast.makeText(this, "Please select both a session and a faculty/student.", Toast.LENGTH_SHORT).show()
            }
        }



    }

    private fun checkEnableNextButton() {
        val nextbtn = findViewById<Button>(R.id.nextbutton)
        nextbtn.isEnabled = isSessionSelected && isPersonSelected
    }

    override fun onBackPressed() {
        super.onBackPressed()
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AdminChooseFacultyStudent::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
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
    } // Returns all sessions from academicsessions table

    private fun fetchstudentsdata(callback: (List<String>) -> Unit){
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)

        if (whotosearch == "student"){
            Log.d("whotosearch", "Fetched JSON Data: $whotosearch")
            val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Courses/fetchstudentsinadmincourses.php"
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apigetcohorts,
                null,
                { response ->
                    Log.d("COURSESADMIN", "Fetched JSON Data: $response")
                    try {
                        val allstudents = mutableListOf<String>()
                        for (i in 0 until response.length()) {
                            val jsonObject = response.getJSONObject(i)
                            val STUDENT = jsonObject.get("Name")
                            allstudents.add(STUDENT.toString())

                        }
                        callback(allstudents)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        callback(emptyList()) // In case of error, return an empty list
                    }
                },
                { error ->
                    Log.e("COURSESADMIN", "Error fetching the data: ${error.message}")
                    callback(emptyList()) // In case of error, return an empty list
                }
            )

            reqQueue.add(jsonArrayRequest)

        }

        else if (whotosearch == "faculty")
        {
            Log.d("whotosearch", "Fetched JSON Data: $whotosearch")
            val reqQueue: RequestQueue = Volley.newRequestQueue(this)
            val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Courses/fetchfacultyinadmincourses.php"

            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apigetcohorts,
                null,
                { response ->
                    Log.d("COURSESADMIN", "Fetched JSON Data: $response")
                    try {
                        val allfaculty = mutableListOf<String>()
                        for (i in 0 until response.length()) {
                            val jsonObject = response.getJSONObject(i)
                            val FACULTY = jsonObject.getString("FacultyName")
                            allfaculty.add(FACULTY)

                        }
                        callback(allfaculty)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        callback(emptyList()) // In case of error, return an empty list
                    }
                },
                { error ->
                    Log.e("COURSESADMIN", "Error fetching the data: ${error.message}")
                    callback(emptyList()) // In case of error, return an empty list
                }
            )

            reqQueue.add(jsonArrayRequest)
        }
    }

    private fun fetchcourses(onFetchComplete: () -> Unit){
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val studentIDint = userID.toIntOrNull()?: 1

        if (whotosearch == "student") {
            Log.d("FETCHSTU", "we are here")
            Log.d("FETCHSTU1", "Selected people is: $selectedpeople | selected desc is: $selectedSessionDescription")
            val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Courses/fetchstudentcoursesinadmin.php?studentname=$selectedpeople&semesterDescription=$selectedSessionDescription"

            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apigetcohorts,
                null,
                { response ->
                    Log.d("STUDENTSFETCHED", "Fetched Courses are: $response")
                    try {
                        coursesList.clear() // Clearing previous courses
                        for (i in 0 until response.length()) {
                            val jsonObject = response.getJSONObject(i)
                            val courseName = jsonObject.getString("Name")
                            coursesList.add(courseName)
                        }
                        onFetchComplete()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        onFetchComplete()
                    }
                },
                { error ->
                    Log.e("StudentsDataFetchCourses", "Error fetching the data in FetchCourses: ${error.message}")
                    onFetchComplete()
                }
            )
            reqQueue.add(jsonArrayRequest)
        }

        else if (whotosearch == "faculty"){
            Log.d("FETCHFACU", "we are here")
            Log.d("FETCHSTU1", "Selected people is: $selectedpeople | selected desc is: $selectedSessionDescription")
            val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Courses/fetchfacultycoursesinadmin.php?FacultyName=$selectedpeople&semesterDescription=$selectedSessionDescription"

            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apigetcohorts,
                null,
                { response ->
                    Log.d("FACULTYFETCHED", "Fetched Courses are: $response")
                    try {
                        coursesList.clear() // Clearing previous courses
                        for (i in 0 until response.length()) {
                            val jsonObject = response.getJSONObject(i)
                            val courseName = jsonObject.getString("Name")
                            coursesList.add(courseName)
                        }
                        onFetchComplete()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        onFetchComplete()
                    }
                },
                { error ->
                    Log.e("StudentsDataFetchCourses", "Error fetching the data in FetchCourses: ${error.message}")
                    onFetchComplete()
                }
            )
            reqQueue.add(jsonArrayRequest)
        }


    }

    private fun navigateToCoursesScreen() {
        val intent = Intent(this, CoursesScreenFirstAdmin::class.java).apply {
            putStringArrayListExtra("COURSES_LIST", coursesList)
            Log.d("CoursesLIST", "Courses list going through: $coursesList")
            putExtra("USER_TYPE", userType)
            putExtra("USER_ID", userID)
            putExtra("WHO_TO_SEARCH", whotosearch)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}