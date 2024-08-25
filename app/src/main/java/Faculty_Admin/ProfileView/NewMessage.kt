package Faculty_Admin.ProfileView

import Faculty_Admin.Announcement.CheckboxAdapter
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.R
import org.json.JSONException
import org.json.JSONObject
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class NewMessage : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID: String

    private lateinit var submitButton: Button
    private lateinit var title: EditText
    private lateinit var content: EditText
    private lateinit var selectedCohorts: List<String>
    private lateinit var select_students_list: List<String>

    private lateinit var cohortDialog: Dialog
    private lateinit var studentDialog: Dialog

    private lateinit var select_cohorts: TextView
    private lateinit var select_students: TextView

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
        setContentView(R.layout.activity_new_message)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()

        select_cohorts = findViewById(R.id.select_cohorts)
        select_students = findViewById(R.id.select_students)
        submitButton = findViewById(R.id.submitbtn)
        title = findViewById(R.id.announcementtitle)
        content = findViewById(R.id.announcementcontent)
        select_students_list = emptyList() // setting to empty initially
        selectedCohorts = emptyList()

        val backbtn = findViewById<ImageButton>(R.id.back_button)
        backbtn.setOnClickListener {
            val intent = Intent(this, Messages::class.java).apply {
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
                            addRecipients(selectedCohorts, select_students_list, Title, Content)
                        }
                    }

                }

            }

        }



    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Messages::class.java).apply {
            putExtra("USER_TYPE", userType)
            putExtra("USER_ID", userID)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun fetchCohorts(callback: (List<String>) -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
//        val apiGetCohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Announcements/Admin/fetch_cohorts.php"
        val apiGetCohorts = "${LoginScreen.BASE_URL}/geceapi/fetch_cohortsN.php"
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
//            val apiGetStudents = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Announcements/Admin/fetch_students_by_cohort.php?cohorts=$encodedCohorts"
            val apiGetStudents = "${LoginScreen.BASE_URL}/geceapi/fetch_students_by_cohortN.php?cohorts=$encodedCohorts"
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
    private fun addRecipients(cohorts: List<String>, students: List<String>, title: String, content: String) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val encodedCohorts = URLEncoder.encode(cohorts.joinToString(","), "UTF-8")
        val encodedStudents = URLEncoder.encode(students.joinToString(","), "UTF-8")
//        val apiFetchRollNumbers = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Announcements/Admin/fetchallrolenumbers.php?cohorts=$encodedCohorts&students=$encodedStudents"
        val apiFetchRollNumbers = "${LoginScreen.BASE_URL}/geceapi/fetchallIDsN.php?cohorts=$encodedCohorts&students=$encodedStudents"
        Log.d("apiFetchRollNumbers","URL: $apiFetchRollNumbers")
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
//                        val rollNumbersList = mutableListOf<String>()
                        for (i in 0 until rollNumbers.length()) {
//                            rollNumbersList.add(rollNumbers.getInt(i).toString())
                            val receiverID = rollNumbers.getInt(i).toString()
                            val userIDint = userID.toIntOrNull()
//                            val titlestr = title.toString()
//                            val contentstr = content.toString()
                            Log.d("msgcontent", "title: $title | content: $content")
                            if (userIDint != null) {
                                insertIntoSendNotifications(userIDint, title, content, formattedDateTime, receiverID)
                            }
                        }

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

    private fun insertIntoSendNotifications(userID: Int, title: String, content: String, postedDateTime: String, receiverID: String) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)

        val encodedTitle = URLEncoder.encode(title, "UTF-8")
        val encodedContent = URLEncoder.encode(content, "UTF-8")

        val apiInsertNotification = "${LoginScreen.BASE_URL}/geceapi/create_message_to_studentsN.php" +
                "?userID=$userID" +
                "&Title=$encodedTitle" +
                "&Content=$encodedContent" +
                "&PostedDateTime=$postedDateTime" +
                "&SentTo=1" +  // Assuming '1' represents 'Students'
                "&ReceiverID=$receiverID"
        Log.d("apiInsertNotification", "URL: $apiInsertNotification")

        val stringRequest = StringRequest(
            Request.Method.GET,
            apiInsertNotification,
            { response ->
                Log.d("InsertNotification", "Notification inserted: $response")
                Toast.makeText(this, "Message was sent", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Log.e("InsertNotification", "Error inserting notification: ${error.message}")
                Toast.makeText(this, "Message was not sent", Toast.LENGTH_SHORT).show()
            }
        )

        reqQueue.add(stringRequest)
    }



}