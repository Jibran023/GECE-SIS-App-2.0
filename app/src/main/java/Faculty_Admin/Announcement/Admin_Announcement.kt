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


class Admin_Announcement : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID : String
    private lateinit var selectedSessionDescription: String // Selected Session
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
        setContentView(R.layout.activity_admin_announcement)
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

        val semesterDeptTextView = findViewById<AutoCompleteTextView>(R.id.semesterdept)
        fetchSessionDescriptions { sessionDescriptions ->
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                sessionDescriptions
            )
            semesterDeptTextView.setAdapter(adapter)

        }

        val submitbtn = findViewById<Button>(R.id.submitbtn)
        submitbtn.setOnClickListener {
            val title = announcementTitle.text.toString().trim()
            val content = announcementContent.text.toString().trim()
            val selectedSession = semesterDeptTextView.text.toString().trim()

            selectedSessionDescription = selectedSession

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

    private fun fetchSessionDescriptions(callback: (List<String>) -> Unit) { // CHANGE THIS SO IT RETRIEVES COHORTS
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apigetcohorts = "http://192.168.18.55/geceapi/Student/Courses/fetchsessions.php"

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


    @RequiresApi(Build.VERSION_CODES.O)
    private fun submitAnnouncement(title: String, content: String) { // Method to handle announcement submission
        // Implement the logic for submitting the announcement here
        Log.d("FacultyAnnounce", "Title: $title | Content: $content | Session: $selectedSessionDescription | Posted Time: $formattedDateTime")
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
