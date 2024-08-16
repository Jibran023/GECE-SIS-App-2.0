package Faculty_Admin.Attendance

import Faculty_Admin.Announcement.CheckboxAdapter
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.ScrollView
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
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.R
import com.example.gece_sisapp20.StudentAdapter2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FacultyViewAttendance2 : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID : String

    private lateinit var selectedCohort: String
    private lateinit var selectedCourse: String
    private lateinit var selectedCourseSessionID: String
    private lateinit var selectedCourseID: String
    private lateinit var selectedSecionID: String
    private lateinit var selectedSecionName: String
    private lateinit var selectedDate: String
    private lateinit var selectedFacultyID: String

    private var studentNames: ArrayList<String> = arrayListOf()
    private var attendanceStatuses: ArrayList<String> = arrayListOf()

    private lateinit var recyclerView: RecyclerView
    private lateinit var edit_attendance: FloatingActionButton
    private lateinit var edit_student_attendance: Dialog

    private lateinit var Updated_attendance: MutableMap<String, String>
    @RequiresApi(Build.VERSION_CODES.O)
    val currentDate: LocalDate = LocalDate.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_view_attendance2)

        selectedDate = intent.getStringExtra("SELECTED_DATE").toString()
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()
        selectedCohort = intent.getStringExtra("COHORT").toString()
        selectedCourse = intent.getStringExtra("COURSE").toString()
        selectedCourseID = intent.getStringExtra("COURSEID").toString()
        selectedSecionName = intent.getStringExtra("SECTION").toString()
        selectedSecionID = intent.getStringExtra("SECTIONID").toString()
        selectedCourseSessionID = intent.getStringExtra("SESSIONID").toString()
        selectedFacultyID = intent.getStringExtra("SELECTED_FACULTY_ID").toString()

        val localDate: LocalDate = LocalDate.parse(selectedDate, dateFormatter)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val back_btn = findViewById<ImageButton>(R.id.backButton)
        back_btn.setOnClickListener {
            val intent = Intent(this, FacultyViewAttendance::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                putExtra("COHORT", selectedCohort)
                putExtra("COURSE", selectedCourse)
                putExtra("COURSEID", selectedCourseID)
                putExtra("SECTION", selectedSecionName)
                putExtra("SECTIONID", selectedSecionID)
                putExtra("SESSIONID", selectedCourseSessionID)
                putExtra("SELECTED_FACULTY_ID", selectedFacultyID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        fetchStudentAttendance {
            // After fetching, set up the adapter
            recyclerView.adapter = StudentAttendanceAdapter(studentNames, attendanceStatuses)
        }

        edit_attendance = findViewById(R.id.fab_edit)
        edit_attendance.setOnClickListener{

            if (userType == "faculty")
            {
                if (currentDate.isAfter(localDate)){
                    Toast.makeText(this, "You can no longer mark attendance for this class!.", Toast.LENGTH_SHORT).show()
                }
                else {
                    showStudentRemoveDialog()
                }
            }
            else {
                showStudentRemoveDialog()
            }
        }


    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, FacultyViewAttendance::class.java).apply {
            putExtra("USER_TYPE", userType)
            putExtra("USER_ID", userID)
            putExtra("COHORT", selectedCohort)
            putExtra("COURSE", selectedCourse)
            putExtra("COURSEID", selectedCourseID)
            putExtra("SECTION", selectedSecionName)
            putExtra("SECTIONID", selectedSecionID)
            putExtra("SESSIONID", selectedCourseSessionID)
            putExtra("SELECTED_FACULTY_ID", selectedFacultyID)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun fetchStudentAttendance(callback: (List<String>) -> Unit) {
        if (userType == "faculty"){
            val reqQueue: RequestQueue = Volley.newRequestQueue(this)
            val apiGetCohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Faculty/Attendance/fetch_section_student_attendance.php?FacultyID=$userID&SectionID=$selectedSecionID&Date=$selectedDate"
            Log.d("Fetched Dates", "URL: $apiGetCohorts")
            Log.d("Fetched Dates", "Section Name: $selectedSecionName | SectionID: $selectedSecionID | Date: $selectedDate | FacultyID: $userID")
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apiGetCohorts,
                null,
                { response ->
                    Log.d("Fetched Attendance", "Response: $response")
                    studentNames.clear()
                    attendanceStatuses.clear()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val studentname = jsonObject.getString("Name")
                        val attstatus = jsonObject.getString("AttendanceStatus")
                        studentNames.add(studentname)
                        attendanceStatuses.add(attstatus)
                    }
                    callback(studentNames)
                },
                { error ->
                    Log.e("FetchCohorts", "Error fetching cohorts: ${error.message}")
                    callback(emptyList())
                }
            )
            reqQueue.add(jsonArrayRequest)
        }
        else if (userType == "admin") {
            val reqQueue: RequestQueue = Volley.newRequestQueue(this)
            val apiGetCohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Faculty/Attendance/fetch_section_student_attendance.php?FacultyID=$selectedFacultyID&SectionID=$selectedSecionID&Date=$selectedDate"
            Log.d("Fetched Dates", "URL: $apiGetCohorts")
            Log.d("Fetched Dates", "Section Name: $selectedSecionName | SectionID: $selectedSecionID | Date: $selectedDate | FacultyID: $selectedFacultyID")
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apiGetCohorts,
                null,
                { response ->
                    Log.d("Fetched Attendance", "Response: $response")
                    studentNames.clear()
                    attendanceStatuses.clear()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val studentname = jsonObject.getString("Name")
                        val attstatus = jsonObject.getString("AttendanceStatus")
                        studentNames.add(studentname)
                        attendanceStatuses.add(attstatus)
                    }
                    callback(studentNames)
                },
                { error ->
                    Log.e("FetchCohorts", "Error fetching cohorts: ${error.message}")
                    callback(emptyList())
                }
            )
            reqQueue.add(jsonArrayRequest)
        }
    }

    private fun showStudentRemoveDialog() {
        fetchStudentAttendance { students ->
            Updated_attendance = mutableMapOf()

            edit_student_attendance = editAttendanceCheckboxDialog(
                this, "Mark Attendance", students, Updated_attendance
            ) { selectedItemsMap ->
                // Process the selected attendance statuses
                Log.d("UpAttendance", "Attendance: $selectedItemsMap")
                markAttendance {
                    // After marking attendance, fetch the updated attendance
                    Toast.makeText(this, "Attendance Updated!", Toast.LENGTH_SHORT).show()
                    fetchStudentAttendance {
                        // After fetching, set up the adapter again to reflect the changes
                        recyclerView.adapter = StudentAttendanceAdapter(studentNames, attendanceStatuses)
                    }
                }
            }
            edit_student_attendance.show()
        }
    }

    private fun editAttendanceCheckboxDialog(context: Context, title: String, items: List<String>, selectedItems: MutableMap<String, String>, onItemsSelected: (selectedItems: Map<String, String>) -> Unit): Dialog {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_list_checkboxes)

        val titleView = dialog.findViewById<TextView>(R.id.dialog_title)
        titleView.text = title

        val listView = dialog.findViewById<ListView>(R.id.listView)
        val adapter = CheckBoxAttendanceAdapter(this,items, selectedItems)
        listView.adapter = adapter

        val applyButton = dialog.findViewById<Button>(R.id.apply_button)
        applyButton.setOnClickListener {
            if (selectedItems.isEmpty()) {
                Toast.makeText(context, "No attendance status selected!", Toast.LENGTH_SHORT).show()
            } else {
                // Show confirmation dialog
                val confirmationDialog = Dialog(context)
                confirmationDialog.setContentView(R.layout.dialog_confirmation)

                val confirmText = confirmationDialog.findViewById<TextView>(R.id.confirmation_text)
                val studentsListText = selectedItems.entries.joinToString("\n") {
                    "Student: ${it.key}, Status: ${it.value}"
                }
                confirmText.text = "Are you sure you want to confirm this attendance?\n\n$studentsListText"

                val yesButton = confirmationDialog.findViewById<Button>(R.id.yes_button)
                val noButton = confirmationDialog.findViewById<Button>(R.id.no_button)

                yesButton.setOnClickListener {
                    // Handle the user's confirmation
                    onItemsSelected(selectedItems)
//                    markAttendance()
                    confirmationDialog.dismiss() // Close the confirmation dialog
                    dialog.dismiss() // Close the main dialog
                }

                noButton.setOnClickListener {
                    // Dismiss the confirmation dialog without making any changes
                    confirmationDialog.dismiss()
                }

                confirmationDialog.show()
            }
        }
        // Adjust the dialog size
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return dialog
    }

    private fun markAttendance(onComplete: () -> Unit) {
        var requestsCompleted = 0
        val totalRequests = Updated_attendance.size
        for ((name, status) in Updated_attendance) {
            val apiUrl = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Faculty/Attendance/update_attendance.php?AttendanceStatus=$status&Name=$name&Date=$selectedDate&SessionID=$selectedCourseSessionID&CourseID=$selectedCourseID&SectionID=$selectedSecionID"
            Log.d("attendanceurl", "URL: $apiUrl")
            val requestBody = JSONObject()

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                apiUrl,
                requestBody,
                { response ->
                    // Handle the response if needed
                    Log.d("MarkAttendance", "Successfully marked attendance for $name with status $status")
                    requestsCompleted++
                    // If all requests are completed, trigger the callback
                    if (requestsCompleted == totalRequests) {
                        onComplete()
                    }
                },
                { error ->
                    Log.e("MarkAttendance", "Failed to mark attendance for $name: ${error.message}")
                    requestsCompleted++
                    // Even if there's an error, check if all requests are completed
                    if (requestsCompleted == totalRequests) {
                        onComplete()
                    }
                }
            )

            // Add the request to the request queue
            val requestQueue: RequestQueue = Volley.newRequestQueue(this)
            requestQueue.add(jsonObjectRequest)
        }
        // Handle the case where there are no updates to be made
        if (totalRequests == 0) {
            onComplete()
        }
    }
}