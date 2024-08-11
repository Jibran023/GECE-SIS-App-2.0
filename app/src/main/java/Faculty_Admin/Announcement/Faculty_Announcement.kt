package Faculty_Admin.Announcement

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
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


class Faculty_Announcement : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID: String
    private lateinit var selectedSessionDescription: String // Selected Session
    private lateinit var selectedcourse: String
    private lateinit var facCourseID: String
    private lateinit var selectedsection: String
    private lateinit var selectedsectionid: String
    private lateinit var announcementTitle: EditText
    private lateinit var announcementContent: EditText
    private lateinit var yearSpinner: Spinner
    private lateinit var courseSpinner: Spinner
    private lateinit var sectionSpinner: Spinner
    private var dataLoaded = false


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
        setContentView(R.layout.activity_faculty_announcement)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()

        val backbtn = findViewById<ImageButton>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, Faculty_Announcement2::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
        val deletebtn = findViewById<Button>(R.id.deletebtn)
        deletebtn.setOnClickListener {
            // Clear the announcement title and content
            announcementTitle.text.clear()
            announcementContent.text.clear()
        }

        Log.d("FacultyAnnounce1", "User Type is: $userType | User ID is: $userID")

        announcementTitle = findViewById(R.id.announcementtitle)
        announcementContent = findViewById(R.id.announcementcontent)

        yearSpinner = findViewById(R.id.year_spinner)
        courseSpinner = findViewById(R.id.course_spinner)
        sectionSpinner = findViewById(R.id.section_spinner)

        var hasCourses = false

        // Fetch sessions and populate the yearSpinner
        fetchSessionDescriptions { sessionDescriptions ->
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                sessionDescriptions
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            yearSpinner.adapter = adapter
        }

        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedYear = parent?.getItemAtPosition(position).toString()
                selectedSessionDescription = parent?.getItemAtPosition(position) as String
                Toast.makeText(this@Faculty_Announcement, "Selected: $selectedSessionDescription", Toast.LENGTH_SHORT).show()
                // Fetch courses based on the selected year
                fetchCourses { courses ->
                    val courseAdapter = ArrayAdapter(this@Faculty_Announcement, android.R.layout.simple_spinner_item, courses)
                    courseSpinner.adapter = courseAdapter
                    // Check if there are courses
                    hasCourses = courseAdapter.count > 0
                    dataLoaded = true
                    Log.d("NoOfCourseS", "Number of Courses: $hasCourses")
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }


        // Set up the course spinner listener
        courseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedcourse = parent?.getItemAtPosition(position).toString()
                if (dataLoaded) {
                    if (selectedcourse == "None") {
                        // If "None" is selected, clear the section spinner and set section to "None"
                        val emptySections: List<String> = listOf("None")
                        val sectionAdapter = ArrayAdapter(
                            this@Faculty_Announcement,
                            android.R.layout.simple_spinner_item,
                            emptySections
                        )
                        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        sectionSpinner.adapter = sectionAdapter
                        selectedsection = "None" // Set selectedsection to "None"
                    } else {
                        if (hasCourses) {
                            fetchSections { sections ->
                                Log.d("SectionsList", "Sections now: $sections")
                                val sectionAdapter = ArrayAdapter(
                                    this@Faculty_Announcement,
                                    android.R.layout.simple_spinner_item,
                                    sections
                                )
                                sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                sectionSpinner.adapter = sectionAdapter
                            }
                        }
//                        else {
//                            val emptySections: List<String> = emptyList()
//                            val sectionAdapter = ArrayAdapter(
//                                this@Faculty_Announcement,
//                                android.R.layout.simple_spinner_item,
//                                emptySections
//                            )
//                            sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                            sectionSpinner.adapter = sectionAdapter
//                            Toast.makeText(this@Faculty_Announcement, "No sections available", Toast.LENGTH_SHORT).show()
//                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        sectionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedsection = parent?.getItemAtPosition(position).toString()
                Log.d("SelectedSection", "Selected Section: $selectedsection")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when no section is selected (optional)
                selectedsection = ""
            }
        }


        val submitbtn = findViewById<Button>(R.id.submitbtn)
        submitbtn.setOnClickListener {
            val title = announcementTitle.text.toString().trim()
            val content = announcementContent.text.toString().trim()

            if (selectedsection.isEmpty()) {
                selectedsection = "None"
            }

            if (selectedcourse == "None")
            {
                Toast.makeText(this, "Please select a Course!", Toast.LENGTH_SHORT).show()
            }
            else{
                Log.d("FacultyStdAn", "Title: $title | Content: $content | Session: $selectedSessionDescription | Course: $selectedcourse | Section: $selectedsection | Posted Time: $formattedDateTime | FacultyCourseID: $facCourseID")
                submitAnnouncement(title, content)
            }




            // Handle the announcement submission
//            submitAnnouncement(title, content)
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Faculty_Announcement2::class.java).apply {
            putExtra("USER_TYPE", userType)
            putExtra("USER_ID", userID)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun fetchSessionDescriptions(callback: (List<String>) -> Unit) {
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

    private fun fetchCourses(callback: (List<String>) -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val studentIDint = userID.toIntOrNull() ?: 1
        if (userType == "faculty") {
            val apigetcohorts = "http://192.168.18.55/geceapi/Student/Courses/fetchcoursesfaculty.php?facultyID=$studentIDint&semesterDescription=$selectedSessionDescription"
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apigetcohorts,
                null,
                { response ->
                    Log.d("FACCOURSE", "Fetched Courses are: $response")
                    try {
                        val courses = mutableListOf<String>()
                        if (response.length() == 0) {
                            courses.add("None")
                        } else {
                            for (i in 0 until response.length()) {
                                val jsonObject = response.getJSONObject(i)
                                val courseName = jsonObject.getString("Name")
                                val facultyCourseID = jsonObject.getString("FAC")
                                courses.add(courseName)
                                facCourseID = facultyCourseID
                            }

                        }
                        callback(courses)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        callback(listOf("None")) // In case of error, return "None"
                    }
                },
                { error ->
                    Log.e("StudentsDataFetchCourses", "Error fetching the data in FetchCourses: ${error.message}")
                    callback(emptyList()) // In case of error, return an empty list
                }
            )
            reqQueue.add(jsonArrayRequest)
        }
    }

    private fun fetchSections(callback: (List<String>) -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        // Construct the API URL with selected session and course
        val apigetsections = "http://192.168.18.55/geceapi/Faculty_Admin/Announcements/Faculty/fetch_sections.php?facultyID=$userID&courseName=$selectedcourse&sessionDescription=$selectedSessionDescription"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apigetsections,
            null,
            { response ->
                Log.d("SECTION_FETCH", "Fetched Sections: $response")
                try {
                    val sections = mutableListOf<String>()
                    if (response.length() == 0) {
                        sections.add("None")
                    }
                    else
                    {
                        for (i in 0 until response.length()) {
                            val sectionName = response.getString(i)
                            sections.add(sectionName)
                        }
                    }
                    callback(sections)
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback(emptyList()) // In case of error, return an empty list
                }
            },
            { error ->
                Log.e("SECTION_FETCH", "Error fetching the data: ${error.message}")
                callback(emptyList()) // In case of error, return an empty list
            }
        )

        reqQueue.add(jsonArrayRequest)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun submitAnnouncement(title: String, content: String) { // Method to handle announcement submission
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)

        val encodedTitle = URLEncoder.encode(title, "UTF-8")
        val encodedContent = URLEncoder.encode(content, "UTF-8")

        // API for creating announcement
        val apiCreateAnnouncement = "http://192.168.18.55/geceapi/Faculty_Admin/Announcements/Faculty/sendanntostudents.php" +
                "?userID=$facCourseID" +
                "&Title=$encodedTitle" +
                "&Content=$encodedContent" +
                "&PostedDateTime=$formattedDateTime"

        val stringRequest = StringRequest(
            Request.Method.GET,
            apiCreateAnnouncement,
            { response ->
                try {
                    Log.d("AnnWhere", "Response: $response")
                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getBoolean("success")
                    if (success) {
                        val announcementID = jsonResponse.getInt("announcementID")
                        if (selectedcourse != "None" && selectedsection == "None") {
                            // Handle "All" case: No need to add individual recipients
                            Log.d("AnnWhere", "Announcement made to all")
                            addAllRecipients(announcementID)
                        } else if (selectedcourse != "None" && selectedsection != "None"){
                            // Handle adding specific faculty members
                            Log.d("AnnWhere", "Announcement made to section")
                            addRecipients(announcementID, selectedsection)
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

    private fun addAllRecipients(announcementID: Int) {

        val apiGetRollNumbers = "http://192.168.18.55/geceapi/Faculty_Admin/Announcements/Faculty/get_roll_numbers.php?CourseName=$selectedcourse&SessionDescription=$selectedSessionDescription"

        Log.d("URLUSED", "Url that is used for roll numbers: $apiGetRollNumbers")
        val stringRequest = StringRequest(
            Request.Method.GET,
            apiGetRollNumbers,
            { response ->
                try {
                    Log.d("GetRollNumbers", "Response: $response")
                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getBoolean("success")
                    if (success) {
                        val rollNumbersArray = jsonResponse.getJSONArray("rollNumbers")
                        val rollNumbers = mutableListOf<String>()
                        for (i in 0 until rollNumbersArray.length()) {
                            rollNumbers.add(rollNumbersArray.getString(i))
                        }

                        val apiAddRecipients = "http://192.168.18.55/geceapi/Faculty_Admin/Announcements/Faculty/sendanntostudentsall.php?announcementID=$announcementID&rollNumbers=${rollNumbers.joinToString(",")}"
                        Log.d("URLUSED", "Url that is used to add recipients: $apiAddRecipients")

                        // Call the second script to add the recipients
                        val addRecipientRequest = StringRequest(
                            Request.Method.GET,
                            apiAddRecipients,
                            { responseAdd ->
                                try {
                                    Log.d("AddRecipients", "Response: $responseAdd")
                                    val jsonResponseAdd = JSONObject(responseAdd)
                                    if (jsonResponseAdd.getBoolean("success")) {
                                        Log.d("AddRecipients", "Recipients added successfully")
                                        Toast.makeText(this, "Announcement made to all students of this course!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Log.e("AddRecipients", "Failed to add recipients: ${jsonResponseAdd.getString("message")}")
                                    }
                                } catch (e: JSONException) {
                                    Log.e("AddRecipients", "Error parsing JSON response: ${e.message}")
                                }
                            },
                            { errorAdd ->
                                Log.e("AddRecipients", "Error adding recipients: ${errorAdd.message}")
                            }
                        )
                        Volley.newRequestQueue(this).add(addRecipientRequest)

                    } else {
                        Log.e("GetRollNumbers", "Failed to retrieve roll numbers: ${jsonResponse.getString("message")}")
                    }
                } catch (e: JSONException) {
                    Log.e("GetRollNumbers", "Error parsing JSON response: ${e.message}")
                }
            },
            { error ->
                Log.e("GetRollNumbers", "Error retrieving roll numbers: ${error.message}")
            }
        )

        Volley.newRequestQueue(this).add(stringRequest)
    }

    private fun addRecipients(announcementID: Int, sectionName: String) {

        // First API for retrieving roll numbers based on section name
        val apiGetRollNumbers = "http://192.168.18.55/geceapi/Faculty_Admin/Announcements/Faculty/get_roll_numbers_bysection.php?CourseName=$selectedcourse&SessionDescription=$selectedSessionDescription&SectionName=$sectionName"

        Log.d("URLUSED", "Url that is used for getting roll numbers: $apiGetRollNumbers")

        val rollNumbersRequest = StringRequest(
            Request.Method.GET,
            apiGetRollNumbers,
            { response ->
                try {
                    Log.d("GetRollNumbers", "Response: $response")
                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getBoolean("success")

                    if (success) {
                        // Extract the list of roll numbers
                        val rollNumbers = jsonResponse.getJSONArray("rollNumbers")
                        val secID = jsonResponse.getJSONArray("sectionID").getInt(0)
                        selectedsectionid = secID.toString()

                        // Prepare the roll numbers as a parameter for the second API
                        val rollNumbersArray = Array(rollNumbers.length()) { i ->
                            rollNumbers.getInt(i)
                        }

                        // Call the second API to add recipients
                        addRecipientsToAnnouncement(announcementID, rollNumbersArray)
                    } else {
                        Log.e("GetRollNumbers", "Failed to retrieve roll numbers: ${jsonResponse.getString("message")}")
                    }
                } catch (e: JSONException) {
                    Log.e("GetRollNumbers", "Error parsing JSON response: ${e.message}")
                }
            },
            { error ->
                Log.e("GetRollNumbers", "Error retrieving roll numbers: ${error.message}")
            }
        )

        Volley.newRequestQueue(this).add(rollNumbersRequest)
    }

    private fun addRecipientsToAnnouncement(announcementID: Int, rollNumbers: Array<Int>) {
        // Convert roll numbers array to a comma-separated string
        val rollNumbersStr = rollNumbers.joinToString(",")

        // Second API for adding recipients based on the retrieved roll numbers
        val apiAddRecipients = "http://192.168.18.55/geceapi/Faculty_Admin/Announcements/Faculty/sendanntostudentsbysection.php?announcementID=$announcementID&rollNumbers=$rollNumbersStr&sectionID=$selectedsectionid"

        Log.d("URLUSED", "Url that is used for adding recipients: $apiAddRecipients")

        val stringRequest = StringRequest(
            Request.Method.GET,
            apiAddRecipients,
            { response ->
                try {
                    Log.d("AddRecipients", "Response: $response")
                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getBoolean("success")
                    if (success) {
                        Log.d("AddRecipients", "Recipients added successfully")
                        Toast.makeText(this, "Announcement made to all students of this section!", Toast.LENGTH_SHORT).show()
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




