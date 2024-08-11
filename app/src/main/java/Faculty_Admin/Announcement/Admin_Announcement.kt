package Faculty_Admin.Announcement

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.MultiAutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.R
import org.json.JSONException
import org.json.JSONObject
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


//class Admin_Announcement : AppCompatActivity() {
//    private var userType: String? = null
//    private lateinit var userID : String
//    private lateinit var selectedSessionDescription: String // Selected Session
//    private lateinit var announcementTitle: EditText
//    private lateinit var announcementContent: EditText
//    private var isAllSelected = false // Track if "All" was selected
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    val currentDateTime = LocalDateTime.now()
//    @RequiresApi(Build.VERSION_CODES.O)
//    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//    @RequiresApi(Build.VERSION_CODES.O)
//    val formattedDateTime = currentDateTime.format(formatter)
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_admin_announcement)
//        userType = intent.getStringExtra("USER_TYPE")
//        userID = intent.getStringExtra("USER_ID").toString()
//
//        val backbtn = findViewById<ImageButton>(R.id.backbtn)
//        backbtn.setOnClickListener {
//            val intent = Intent(this, AdminChooseAnnouncement::class.java).apply {
//                putExtra("USER_TYPE", userType)
//                putExtra("USER_ID", userID)
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            }
//            startActivity(intent)
//        }
//
//        Log.d("FacultyAnnounce1", "User Type is: $userType | User ID is: $userID")
//
//        announcementTitle = findViewById(R.id.announcementtitle)
//        announcementContent = findViewById(R.id.announcementcontent)
//
//
//        val deletebtn = findViewById<Button>(R.id.deletebtn)
//        deletebtn.setOnClickListener {
//            // Clear the announcement title and content
//            announcementTitle.text.clear()
//            announcementContent.text.clear()
//        }
//
//        val semesterDeptTextView = findViewById<AutoCompleteTextView>(R.id.semesterdept)
//        fetchSessionDescriptions { sessionDescriptions ->
//            val adapter = ArrayAdapter(
//                this,
//                android.R.layout.simple_dropdown_item_1line,
//                sessionDescriptions
//            )
//            semesterDeptTextView.setAdapter(adapter)
//
//        }
//
//        val submitbtn = findViewById<Button>(R.id.submitbtn)
//        submitbtn.setOnClickListener {
//            val title = announcementTitle.text.toString().trim()
//            val content = announcementContent.text.toString().trim()
//            val selectedSession = semesterDeptTextView.text.toString().trim()
//
//            selectedSessionDescription = selectedSession
//
//            // Handle the announcement submission
//            submitAnnouncement(title, content)
//        }
//
//
//    }
//
//    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent(this, AdminChooseAnnouncement::class.java).apply {
//            putExtra("USER_TYPE", userType)
//            putExtra("USER_ID", userID)
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        startActivity(intent)
//    }
//
//    private fun fetchSessionDescriptions(callback: (List<String>) -> Unit) { // CHANGE THIS SO IT RETRIEVES COHORTS
//        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
//        val apigetcohorts = "http://192.168.18.55/geceapi/Student/Courses/fetchsessions.php"
//
//        val jsonArrayRequest = JsonArrayRequest(
//            Request.Method.GET,
//            apigetcohorts,
//            null,
//            { response ->
//                Log.d("SessionData", "Fetched JSON Data: $response")
//                try {
//                    val sessionDescriptions = mutableListOf<String>()
//                    for (i in 0 until response.length()) {
//                        val jsonObject = response.getJSONObject(i)
//                        val description = jsonObject.getString("Description")
//                        sessionDescriptions.add(description)
//                    }
//                    callback(sessionDescriptions)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    callback(emptyList()) // In case of error, return an empty list
//                }
//            },
//            { error ->
//                Log.e("SessionData", "Error fetching the data: ${error.message}")
//                callback(emptyList()) // In case of error, return an empty list
//            }
//        )
//        reqQueue.add(jsonArrayRequest)
//    } // Returns all sessions from academicsessions table
//
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun submitAnnouncement(title: String, content: String) { // Method to handle announcement submission
//        // Implement the logic for submitting the announcement here
//        Log.d("FacultyAnnounce", "Title: $title | Content: $content | Session: $selectedSessionDescription | Posted Time: $formattedDateTime")
//    }
//
//    private fun fetchstudentsdata(callback: (List<String>) -> Unit){
//        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
//        val apigetcohorts = "http://192.168.18.55/geceapi/Faculty_Admin/Courses/fetchstudentsinadmincourses.php"
//        val jsonArrayRequest = JsonArrayRequest(
//            Request.Method.GET,
//            apigetcohorts,
//            null,
//            { response ->
//                Log.d("COURSESADMIN", "Fetched JSON Data: $response")
//                try {
//                    val allstudents = mutableListOf<String>()
//                    for (i in 0 until response.length()) {
//                        val jsonObject = response.getJSONObject(i)
//                        val STUDENT = jsonObject.get("Name")
//                        allstudents.add(STUDENT.toString())
//                    }
//                    callback(allstudents)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    callback(emptyList()) // In case of error, return an empty list
//                }
//            },
//            { error ->
//                Log.e("COURSESADMIN", "Error fetching the data: ${error.message}")
//                callback(emptyList()) // In case of error, return an empty list
//            }
//        )
//        reqQueue.add(jsonArrayRequest)
//    }
//
//}

class Admin_Announcement : AppCompatActivity() {
    private lateinit var cohortDropdown: MultiAutoCompleteTextView
    private lateinit var studentDropdown: MultiAutoCompleteTextView
    private lateinit var submitButton: Button
    private lateinit var deleteButton: Button

    private lateinit var title: EditText
    private lateinit var content: EditText

    private var userType: String? = null
    private lateinit var userID: String

    private var isAllCohortsSelected = false
    private var isAllStudentsSelected = false

    private lateinit var selectedcohorts: List<String>

    @RequiresApi(Build.VERSION_CODES.O)
    val currentDateTime = LocalDateTime.now()

    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @RequiresApi(Build.VERSION_CODES.O)
    val formattedDateTime = currentDateTime.format(formatter)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_announcement)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()

        cohortDropdown = findViewById(R.id.semesterdept)
        studentDropdown = findViewById(R.id.studentsdept)
        submitButton = findViewById(R.id.submitbtn)
        deleteButton = findViewById(R.id.deletebtn)
        title = findViewById(R.id.announcementtitle)
        content = findViewById(R.id.announcementcontent)

        val backbtn = findViewById<ImageButton>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, AdminChooseAnnouncement::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // Set tokenizer for MultiAutoCompleteTextView
        cohortDropdown.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        studentDropdown.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        // Initialize adapters only once
        val cohortAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        cohortDropdown.setAdapter(cohortAdapter)

        val studentAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        studentDropdown.setAdapter(studentAdapter)


        fetchCohorts { cohorts ->
            cohortAdapter.clear()
            cohortAdapter.addAll(listOf("All") + cohorts)
            cohortAdapter.notifyDataSetChanged()
        }

        cohortDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedCohort = cohortAdapter.getItem(position).toString()
            Log.d("Selected Cohort", "Selected cohort is: $selectedCohort")
            handleCohortSelection(selectedCohort, cohortAdapter, studentAdapter)
        }

        studentDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedStudent = studentDropdown.adapter.getItem(position).toString()
            if (selectedStudent == "All") {
                isAllStudentsSelected = true
                studentDropdown.setText("All,")
                Log.d("StudentSelection", "Selected student: All")
                // Disable other student options if "All" is selected
                if (isAllCohortsSelected) {
                    studentDropdown.isEnabled = false
                }
            } else {
                if (isAllStudentsSelected) {
                    isAllStudentsSelected = false
                    studentDropdown.setText(selectedStudent)
                }
                Log.d("StudentSelection", "Selected student: $selectedStudent")
                // Re-enable student dropdown if needed
                if (isAllCohortsSelected) {
                    studentDropdown.isEnabled = false
                } else {
                    studentDropdown.isEnabled = true
                }
            }
            studentDropdown.setSelection(studentDropdown.text.length)
        }

        submitButton.setOnClickListener {
            val selectedCohort = cohortDropdown.text.toString().trim()
            val selectedStudent = studentDropdown.text.toString().trim()
            val pastitile = title.text.toString().trim()
            val Content = content.text.toString().trim()

            Log.d("ROSTUDAnnounce", "Selected student: $selectedStudent | SelectedCohort: $selectedCohort")
        }

        deleteButton.setOnClickListener {
            // Handle deletion logic here
            title.text.clear()
            content.text.clear()
        }
    }

    private fun handleCohortSelection(selectedCohort: String, cohortAdapter: ArrayAdapter<String>, studentAdapter: ArrayAdapter<String>) {
        if (selectedCohort == "All") {
            isAllCohortsSelected = true
            cohortDropdown.setText("All,")
            studentDropdown.setText("All,")
            Log.d("Baba Where", "We are in handleCohortSelection")

            fetchStudents(listOf("All")) { students ->
                studentAdapter.clear()
                studentAdapter.addAll(listOf("All") + students)
                studentAdapter.notifyDataSetChanged()
                Log.d("Baba Where", "We are in fetchStudents")
                // Disable student options if "All" is selected in both
                if (isAllStudentsSelected) {
                    studentDropdown.isEnabled = false
                }
            }
        } else {
            val currentText = cohortDropdown.text.toString()
            Log.d("Baba Where", "We are in handleCohortSelection else condition")

            if (currentText.contains("All,")) {
                isAllCohortsSelected = false
                cohortDropdown.setText(currentText.replace("All,", ""))
                studentDropdown.setText("")
            }
            selectedcohorts = cohortDropdown.text.split(",").filter { it.isNotBlank() }
            fetchStudents(selectedcohorts) { students ->
                studentAdapter.clear()
                studentAdapter.addAll(listOf("All") + students)
                studentAdapter.notifyDataSetChanged()
                Log.d("Baba Where", "we are in after if statement")
            }
        }
        cohortDropdown.setSelection(cohortDropdown.text.length)
    }


    private fun fetchCohorts(callback: (List<String>) -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apiGetCohorts =
            "http://192.168.18.55/geceapi/Faculty_Admin/Announcements/Admin/fetch_cohorts.php"

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
                Log.d("Fetcher", "Fetched Cohorts Successfully!")
            },
            { error ->
                Log.e("FetchCohorts", "Error fetching cohorts: ${error.message}")
                callback(emptyList())
            }
        )
        reqQueue.add(jsonArrayRequest)
    }

    private fun fetchStudents(selectedCohorts: List<String>, callback: (List<String>) -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)

        // Join selected cohorts with commas and encode them
        val encodedCohorts = URLEncoder.encode(selectedCohorts.joinToString(","), "UTF-8")
        val apiGetStudents =
            "http://192.168.18.55/geceapi/Faculty_Admin/Announcements/Admin/fetch_students_by_cohort.php?cohorts=$encodedCohorts"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apiGetStudents,
            null,
            { response ->
                Log.d("CohortStds", "Retrieved Students: $response")
                val students = mutableListOf<String>()
                for (i in 0 until response.length()) {
                    // If the response is an array of strings
                    val student = response.getString(i)
                    students.add(student)
                }
                callback(students)
            },
            { error ->
                Log.e("FetchStudents", "Error fetching students: ${error.message}")
                callback(emptyList())
            }
        )
        reqQueue.add(jsonArrayRequest)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, AdminChooseAnnouncement::class.java).apply {
            putExtra("USER_TYPE", userType)
            putExtra("USER_ID", userID)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}