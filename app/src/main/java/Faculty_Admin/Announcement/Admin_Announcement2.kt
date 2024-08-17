package Faculty_Admin.Announcement

import android.annotation.SuppressLint
import android.content.Intent
import java.net.URLEncoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Admin_Announcement2 : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID : String
    private lateinit var selectedFacultyMembers: List<String> // Selected Faculty Members
    private lateinit var announcementTitle: EditText
    private lateinit var announcementContent: EditText
    private var isAllSelected = false // Track if "All" was selected

    @RequiresApi(Build.VERSION_CODES.O)
    val currentDateTime = LocalDateTime.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    @RequiresApi(Build.VERSION_CODES.O)
    val formattedDateTime = currentDateTime.format(formatter)

    @SuppressLint("MissingInflatedId")
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
//        val studentDeptTextView = findViewById<MultiAutoCompleteTextView>(R.id.studentsdept)

        fetchfacultymembers { facultymembers ->
            if (facultymembers.isNotEmpty()) {
                // Add the "All" option
                val modifiedFacultyMembers = listOf("All") + facultymembers
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_dropdown_item_1line,
                    modifiedFacultyMembers
                )
                semesterDeptTextView.setAdapter(adapter)
                semesterDeptTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

                semesterDeptTextView.setOnItemClickListener { _, _, position, _ ->
                    val selectedOption = adapter.getItem(position)

                    if (selectedOption == "All") {
                        // Clear any other selections
                        isAllSelected = true
                        semesterDeptTextView.setText("All,")
                        Log.d("AllorNot", "Selected options was all")
                    } else {
                        // Remove "All" if it was selected
                        val currentText = semesterDeptTextView.text.toString()
                        if (currentText.contains("All,")) {
                            isAllSelected = false
                            semesterDeptTextView.setText(currentText.replace("All,", ""))
                        }
                        Log.d("AllorNot", "Selected options was all")
                    }
                    semesterDeptTextView.setSelection(semesterDeptTextView.text.length)
                }
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
            selectedFacultyMembers = selectedFacultyString.split(",").map { it.trim() }.filter { it.isNotEmpty() }


            // Reevaluate the "All" selection
            isAllSelected = selectedFacultyMembers.contains("All") && selectedFacultyMembers.size == 1
            // Handle the announcement submission
            if (title.isNotEmpty() && content.isNotEmpty() && selectedFacultyMembers.isNotEmpty()) {
                // Handle the announcement submission
                submitAnnouncement(title, content)

                // Show a toast message
                val message = if (isAllSelected) {
                    "Announcement will be sent to all faculty members."
                } else {
                    "Announcement will be sent to the selected faculty members."
                }
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            } else {
                // Show an error toast if any required fields are empty
                Toast.makeText(this, "Please fill in all fields and select at least one faculty member.", Toast.LENGTH_SHORT).show()
            }
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
        val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Announcements/Admin/fetchfacultymembers.php"

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
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)

        val encodedTitle = URLEncoder.encode(title, "UTF-8")
        val encodedContent = URLEncoder.encode(content, "UTF-8")

        // API for creating announcement
        val apiCreateAnnouncement = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Announcements/Admin/create_announcement_for_faculty.php" +
                "?userID=$userID" +
                "&Title=$encodedTitle" +
                "&Content=$encodedContent" +
                "&PostedDateTime=$formattedDateTime"
        Log.d("apiCreateAnnouncement", "URL: $apiCreateAnnouncement")

        val stringRequest = StringRequest(
            Request.Method.GET,
            apiCreateAnnouncement,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getBoolean("success")
                    if (success) {
                        val announcementID = jsonResponse.getInt("announcementID")
                        if (isAllSelected) {
                            // Handle "All" case: No need to add individual recipients
                            Log.d("AnnWhere", "Announcement made to all")
                            addAllRecipients(announcementID)
                        } else {
                            // Handle adding specific faculty members
                            Log.d("AnnWhere", "Announcement made to specific")
                            addRecipients(announcementID)
                        }

                        Log.d("AnnID", "Announcement ID is: $announcementID")
                    } else {
                        Log.e("SubmitAnnouncement", "Failed to submit announcement: ${jsonResponse.getString("message")}")
                    }
                } catch (e: JSONException) {
                    Log.e("SubmitAnnouncement", "Error parsing JSON response: ${e.message}")
                }
            },
            { error ->
                Log.e("SubmitAnnouncement", "Error creating announcement: ${error.message}")
            }
        )

        reqQueue.add(stringRequest)

    }

    private fun addRecipients(announcementID: Int) {
        val encodedFacultyMembers = URLEncoder.encode(selectedFacultyMembers.joinToString(","), "UTF-8")


        // API for adding recipients
        val apiAddRecipients = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Announcements/Admin/add_faculty_recipients.php" + "?announcementID=$announcementID" +
                "&FacultyMembers=$encodedFacultyMembers"
        Log.d("apiAddRecipients", "URL: $apiAddRecipients")

        Log.d("TOSENTFAC", "Faculty Members: $encodedFacultyMembers")
        val stringRequest = StringRequest(
            Request.Method.GET,
            apiAddRecipients,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getBoolean("success")
                    if (success) {
                        Log.d("AddRecipients", "Recipients added successfully")
                    } else {
                        Log.e("AddRecipients", "Failed to add recipients: ${jsonResponse.getString("message")}")
                    }
                } catch (e: JSONException) {
                    Log.e("AddRecipients", "Error parsing JSON response: ${e.message}")
                }
            },
            { error ->
                Log.e("AddRecipients", "Error adding recipients: ${error.message}")
            }
        )

        Volley.newRequestQueue(this).add(stringRequest)
    }

    private fun addAllRecipients(announcementID: Int) {
        // Implement the logic to add all faculty members as recipients
        // This could involve calling an API that handles sending to all faculty members
        val apiAddRecipients = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Announcements/Admin/addall_faculty_recipients.php" + "?announcementID=$announcementID"
        Log.d("apiAddAllRecipients", "URL: $apiAddRecipients")
        val stringRequest = StringRequest(
            Request.Method.GET,
            apiAddRecipients,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getBoolean("success")
                    if (success) {
                        Log.d("IsAnnounce", "Sending announcement to all faculty members")
                        Log.d("AddRecipients", "Recipients added successfully")
                    } else {
                        Log.e("AddRecipients", "Failed to add recipients: ${jsonResponse.getString("message")}")
                    }
                } catch (e: JSONException) {
                    Log.e("AddRecipients", "Error parsing JSON response: ${e.message}")
                }
            },
            { error ->
                Log.e("AddRecipients", "Error adding recipients: ${error.message}")
            }
        )

        Volley.newRequestQueue(this).add(stringRequest)
    }
}
