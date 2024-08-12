package Faculty_Admin.Announcement

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.MultiAutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

class Admin_Announcement : AppCompatActivity() {
    private lateinit var submitButton: Button
    private lateinit var deleteButton: Button

    private lateinit var title: EditText
    private lateinit var content: EditText

    private var userType: String? = null
    private lateinit var userID: String


    private lateinit var selectedCohorts: List<String>
    private lateinit var select_students_list: List<String>

    private lateinit var cohortDialog: Dialog
    private lateinit var studentDialog: Dialog

    private lateinit var select_cohorts: TextView
    private lateinit var select_students: TextView

    private var isAllSelected = false

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

        select_cohorts = findViewById(R.id.select_cohorts)
        select_students = findViewById(R.id.select_students)

        submitButton = findViewById(R.id.submitbtn)
        deleteButton = findViewById(R.id.deletebtn)
        title = findViewById(R.id.announcementtitle)
        content = findViewById(R.id.announcementcontent)

        select_students_list = emptyList() // setting to empty initially
        selectedCohorts = emptyList()

        val backbtn = findViewById<ImageButton>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, AdminChooseAnnouncement::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        select_cohorts.setOnClickListener{
            showCohortDialog()
        }
        select_students.setOnClickListener{
            showStudentDialog()
        }

        val deletebtn = findViewById<Button>(R.id.deletebtn)
        deletebtn.setOnClickListener {
            // Clear the announcement title and content
            title.text.clear()
            content.text.clear()
        }

        submitButton.setOnClickListener{

            if (selectedCohorts.isEmpty()){
                Toast.makeText(this, "Please Select a Cohort!", Toast.LENGTH_SHORT).show()
            }
            else {
                if(select_students_list.isEmpty())
                {
                    Toast.makeText(this, "Please Select a Student!", Toast.LENGTH_SHORT).show()
                }
                else{
                    val Title = title.text.toString().trim()
                    val Content = content.text.toString().trim()
                    if (Title.isEmpty()){
                        Toast.makeText(this, "Please enter an announcement title!", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        if (Content.isEmpty()){
                            Toast.makeText(this, "Please enter announcement content!", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Log.d("AdminANN", "Title: $Title | Content: $Content | Cohort: ${selectedCohorts.joinToString(", ")} | Students:${select_students_list.joinToString(", ")} ")
                            submitAnnouncement(Title, Content)
                        }
                    }

                }

            }

        }



    }


    private fun fetchCohorts(callback: (List<String>) -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apiGetCohorts = "http://192.168.18.55/geceapi/Faculty_Admin/Announcements/Admin/fetch_cohorts.php"

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

    private fun fetchStudents(selectedCohorts: List<String>, callback: (List<String>) -> Unit) {
        if (selectedCohorts.isEmpty()) {
            // Handle the case where no cohorts are selected
            callback(emptyList())
            return
        }
        else {
            val reqQueue: RequestQueue = Volley.newRequestQueue(this)
            val encodedCohorts = URLEncoder.encode(selectedCohorts.joinToString(","), "UTF-8")
            val apiGetStudents = "http://192.168.18.55/geceapi/Faculty_Admin/Announcements/Admin/fetch_students_by_cohort.php?cohorts=$encodedCohorts"

            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apiGetStudents,
                null,
                { response ->
                    val students = mutableListOf<String>()
                    for (i in 0 until response.length()) {
                        val student = response.getString(i)
                        students.add(student)
                    }
                    students.add("All")
                    callback(students)
                },
                { error ->
                    Log.e("FetchStudents", "Error fetching students: ${error.message}")
                    callback(emptyList())
                }
            )
            reqQueue.add(jsonArrayRequest)
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


    private fun showCohortDialog() {
        fetchCohorts { cohorts ->
            cohortDialog = createDialogWithCheckboxes(
                this, "Select Cohorts", cohorts
            ) { selectedItems ->
                selectedCohorts = selectedItems
                select_cohorts.text = selectedItems.joinToString(", ")
                Log.d("Selected", "Selected Cohorts: ${selectedCohorts.joinToString(", ")}")

                // Clear selected students when a new cohort is selected
                select_students_list = emptyList()
                select_students.text = ""
                select_students.isEnabled = selectedCohorts.isNotEmpty()
            }
            cohortDialog.show()
        }
    }

    private fun showStudentDialog() {
        if (selectedCohorts.isEmpty()) {
            Log.e("ShowStudentDialog", "No cohorts selected")
            return
        } else {
            fetchStudents(selectedCohorts) { students ->
                studentDialog = createDialogWithCheckboxes(
                    this, "Select Students", students
                ) { selectedStudents ->
                    select_students_list = selectedStudents
                    Log.d("ShowStudentDialog", "Selected Students: ${selectedStudents.joinToString(", ")}")
                    select_students.text = selectedStudents.joinToString(", ")
                }
                studentDialog.show()
            }
        }
    }

    private fun createDialogWithCheckboxes(
        context: Context,
        title: String,
        items: List<String>,
        onItemsSelected: (selectedItems: List<String>) -> Unit
    ): Dialog {
        val selectedItems = mutableListOf<String>()
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_list_checkboxes)

        val titleView = dialog.findViewById<TextView>(R.id.dialog_title)
        titleView.text = title

        val listView = dialog.findViewById<ListView>(R.id.listView)
        val adapter = CheckboxAdapter(items, selectedItems)
        listView.adapter = adapter

        val applyButton = dialog.findViewById<Button>(R.id.apply_button)
        applyButton.setOnClickListener {
            if (selectedItems.contains("All")) {
                // If "All" is selected, replace the selection with all items except "All"
                selectedItems.clear()
                selectedItems.addAll(items.filter { it != "All" })
            }
            onItemsSelected(selectedItems)
            dialog.dismiss()
        }

        return dialog
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun submitAnnouncement(title: String, content: String) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)

        val encodedTitle = URLEncoder.encode(title, "UTF-8")
        val encodedContent = URLEncoder.encode(content, "UTF-8")

        // API for creating announcement
        val apiCreateAnnouncement = "http://192.168.18.55/geceapi/Faculty_Admin/Announcements/Admin/create_announcement_for_students.php" +
                "?userID=$userID" +
                "&Title=$encodedTitle" +
                "&Content=$encodedContent" +
                "&PostedDateTime=$formattedDateTime"

        val stringRequest = StringRequest(
            Request.Method.GET,
            apiCreateAnnouncement,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getBoolean("success")
                    if (success) {
                        val announcementID = jsonResponse.getInt("announcementID")
                        addRecipients(announcementID, selectedCohorts, select_students_list)

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


    private fun addRecipients(announcementID: Int, cohorts: List<String>, students: List<String>) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val encodedCohorts = URLEncoder.encode(cohorts.joinToString(","), "UTF-8")
        val encodedStudents = URLEncoder.encode(students.joinToString(","), "UTF-8")
        val apiFetchRollNumbers = "http://192.168.18.55/geceapi/Faculty_Admin/Announcements/Admin/fetchallrolenumbers.php?cohorts=$encodedCohorts&students=$encodedStudents"

        val fetchRollNumbersRequest = StringRequest(
            Request.Method.GET,
            apiFetchRollNumbers,
            { response ->
                try {

                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getBoolean("success")
                    if (success) {
                        Log.d("STDREQ", "ROLL NUMBERS FETCHED: $response")
                        val rollNumbers = jsonResponse.getJSONArray("rollNumbers")
                        val rollNumbersList = mutableListOf<String>()
                        for (i in 0 until rollNumbers.length()) {
                            rollNumbersList.add(rollNumbers.getInt(i).toString())
                        }

                        insertRecipients(announcementID, rollNumbersList)
                    } else {
                        Log.e("STDREQ", "Failed to fetch roll numbers: ${jsonResponse.getString("message")}")
                    }
                } catch (e: JSONException) {
                    Log.e("STDREQ", "Error parsing JSON response: ${e.message}")
                }
            },
            { error ->
                Log.e("STDREQ", "Error fetching roll numbers: ${error.message}")
            }
        )

        reqQueue.add(fetchRollNumbersRequest)
    }

    private fun insertRecipients(announcementID: Int, rollNumbers: List<String>) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val encodedRollNumbers = URLEncoder.encode(rollNumbers.joinToString(","), "UTF-8")
        val apiInsertRecipients = "http://192.168.18.55/geceapi/Faculty_Admin/Announcements/Admin/send_announcement_to_students.php?announcementID=$announcementID&rollNumbers=$encodedRollNumbers"

        val insertRecipientsRequest = StringRequest(
            Request.Method.GET,
            apiInsertRecipients,
            { response ->
                try {
                    Log.d("STDREQ", "Recipients added successfully: $response")
                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getBoolean("success")
                    if (success) {
                        Log.d("STDREQ", "Recipients added successfully")
                        Toast.makeText(this, "Announcement was successfully made to the student(s)!", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("STDREQ", "Failed to add recipients: ${jsonResponse.getString("message")}")
                    }
                } catch (e: JSONException) {
                    Log.e("STDREQ", "Error parsing JSON response: ${e.message}")
                }
            },
            { error ->
                Log.e("STDREQ", "Error adding recipients: ${error.message}")
            }
        )

        reqQueue.add(insertRecipientsRequest)
    }




}

