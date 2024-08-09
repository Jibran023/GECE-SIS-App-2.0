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
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Admin_Announcement2 : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID : String
    private lateinit var selectedFacultyMembers: List<String> // Selected Faculty Members
    private lateinit var announcementTitle: EditText
    private lateinit var announcementContent: EditText

    @RequiresApi(Build.VERSION_CODES.O)
    val currentDateTime = LocalDateTime.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    @RequiresApi(Build.VERSION_CODES.O)
    val formattedDateTime = currentDateTime.format(formatter)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_announcement2)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()

        val backbtn = findViewById<ImageButton>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, AdminChooseAnnouncement::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        Log.d("FacultyAnnounce1", "User Type is: $userType | User ID is: $userID")

        announcementTitle = findViewById(R.id.announcementtitle)
        announcementContent = findViewById(R.id.announcementcontent)


        val deletebtn = findViewById<Button>(R.id.deletebtn)
        deletebtn.setOnClickListener {
            // Clear the announcement title and content
            announcementTitle.text.clear()
            announcementContent.text.clear()
        }

        val semesterDeptTextView = findViewById<MultiAutoCompleteTextView>(R.id.semesterdept)
        fetchfacultymembers { facultymembers ->
            if (facultymembers.isNotEmpty()) {
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_dropdown_item_1line,
                    facultymembers
                )
                semesterDeptTextView.setAdapter(adapter)
                semesterDeptTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
            } else {
                Log.e("SessionData", "No sessions available")
            }
        }

        val submitbtn = findViewById<Button>(R.id.submitbtn)
        submitbtn.setOnClickListener {
            val title = announcementTitle.text.toString().trim()
            val content = announcementContent.text.toString().trim()
            val selectedFacultyString = semesterDeptTextView.text.toString().trim()

            // Split the comma-separated string into a list of selected faculty members
            selectedFacultyMembers = selectedFacultyString.split(",").map { it.trim() }

            // Handle the announcement submission
            submitAnnouncement(title, content)
        }


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

    // CHANGE THIS SO IT RETRIEVES COHORTS
    private fun fetchfacultymembers(callback: (List<String>) -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apigetcohorts = "http://192.168.18.55/geceapi/Faculty_Admin/Announcements/Admin/fetchfacultymembers.php"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apigetcohorts,
            null,
            { response ->
                Log.d("FacultyMembers", "Fetched JSON Data: $response")
                try {
                    val facultymemebers = mutableListOf<String>()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val facultymember = jsonObject.getString("FacultyName")
                        facultymemebers.add(facultymember)
                    }
                    callback(facultymemebers)
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback(emptyList()) // In case of error, return an empty list
                }
            },
            { error ->
                Log.e("FacultyMembers", "Error fetching the data: ${error.message}")
                callback(emptyList()) // In case of error, return an empty list
            }
        )
        reqQueue.add(jsonArrayRequest)
    } // Returns all sessions from academicsessions table


    @RequiresApi(Build.VERSION_CODES.O)
    private fun submitAnnouncement(title: String, content: String) { // Method to handle announcement submission
        // Implement the logic for submitting the announcement here
        Log.d("FacultyAnnounce", "Title: $title | Content: $content | Faculty Members: $selectedFacultyMembers | Posted Time: $formattedDateTime")
    }

    private fun fetchstudentsdata(callback: (List<String>) -> Unit){
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apigetcohorts = "http://192.168.18.55/geceapi/Faculty_Admin/Courses/fetchstudentsinadmincourses.php"
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

}
