package Faculty_Admin.Attendance

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.R
import org.json.JSONObject
import java.util.Calendar


class faculty_markattendance3 : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID : String

    private lateinit var selectedCohort: String
    private lateinit var selectedCourse: String
    private lateinit var selectedCourseSessionID: String
    private lateinit var selectedCourseID: String
    private lateinit var selectedSecionID: String
    private lateinit var selectedSecionName: String
    private lateinit var markattendance: Button

    private lateinit var dayPicker: NumberPicker
    private lateinit var monthPicker: NumberPicker
    private lateinit var yearPicker: NumberPicker

    private var studentNames: ArrayList<String> = arrayListOf()
    private lateinit var Updated_attendance: MutableMap<String, String>
    private lateinit var edit_student_attendance: Dialog
    private lateinit var selectedDate: String
    private var attendance_dates: ArrayList<String> = arrayListOf()
    private lateinit var selectedFacultyID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_markattendance3)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()
        selectedCohort = intent.getStringExtra("COHORT").toString()
        selectedCourse = intent.getStringExtra("COURSE").toString()
        selectedCourseID = intent.getStringExtra("COURSEID").toString()
        selectedSecionName = intent.getStringExtra("SECTION").toString()
        selectedSecionID = intent.getStringExtra("SECTIONID").toString()
        selectedCourseSessionID = intent.getStringExtra("SESSIONID").toString()
        selectedFacultyID = intent.getStringExtra("SELECTED_FACULTY_ID").toString()

        val backbtn = findViewById<ImageView>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, faculty_markattendance1::class.java).apply {
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

        val calendar = Calendar.getInstance() // Get the current date
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // Month is 0-based in Calendar
        val currentYear = calendar.get(Calendar.YEAR)
        dayPicker = findViewById(R.id.day_picker) // Initialize NumberPickers
        monthPicker = findViewById(R.id.month_picker)
        yearPicker = findViewById(R.id.year_picker)
        dayPicker.minValue = currentDay - 1 // Set the range for dayPicker
        dayPicker.maxValue = currentDay
        dayPicker.value = currentDay
        monthPicker.minValue = currentMonth // Set the range for monthPicker
        monthPicker.maxValue = currentMonth
        monthPicker.value = currentMonth
        yearPicker.minValue = currentYear // Set the range for yearPicker
        yearPicker.maxValue = currentYear
        yearPicker.value = currentYear

        markattendance = findViewById(R.id.nextbtn)
        markattendance.setOnClickListener {
            // Get the selected date
            val selectedDay = dayPicker.value
            val selectedMonth = monthPicker.value
            val selectedYear = yearPicker.value

            // Combine the selected date into a string
            selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay)
//            Toast.makeText(this@faculty_markattendance3, "$selectedDate", Toast.LENGTH_SHORT).show()

            // Fetch the attendance dates
            fetchAttendanceDates { fetchedDates ->
                // Flag to check if the selected date is found
                var dateFound = false

                // Log the selected date and fetched dates for debugging
                Log.d("Selected Date", "Selected Date: $selectedDate")
                Log.d("Attendance Dates", "Fetched Dates: $attendance_dates")

                // Loop through the fetched dates to compare with the selected date
                for (date in attendance_dates) {
                    if (date == selectedDate) {
                        // The selected date matches one of the fetched dates
                        dateFound = true
                        Log.d("Date Comparison", "Match found: $date")
                        break
                    }
                }
                if (dateFound) {
                    // The selected date is in the list of fetched dates
                    Toast.makeText(this, "Attendance for this date has already been marked", Toast.LENGTH_SHORT).show()
                } else {
                    // The selected date is not in the list of fetched dates
//                    Toast.makeText(this, "Date is not available, go ahead!", Toast.LENGTH_SHORT).show()
                    showStudentRemoveDialog()
                }
            }

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, faculty_markattendance1::class.java).apply {
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
            val apiGetCohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Faculty/Attendance/fetch_section_students_for_marking.php?FacultyID=$userID&SectionID=$selectedSecionID&Date=$selectedCourseSessionID"
            Log.d("Fetched Dates", "URL: $apiGetCohorts")
            Log.d("Fetched Dates", "Section Name: $selectedSecionName | SectionID: $selectedSecionID | FacultyID: $userID")
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apiGetCohorts,
                null,
                { response ->
                    Log.d("Fetched Attendance", "Response: $response")
                    studentNames.clear()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val studentname = jsonObject.getString("Name")
                        studentNames.add(studentname)
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
            val apiGetCohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Faculty/Attendance/fetch_section_students_for_marking.php?FacultyID=$selectedFacultyID&SectionID=$selectedSecionID&Date=$selectedCourseSessionID"
            Log.d("Fetched Dates", "URL: $apiGetCohorts")
            Log.d("Fetched Dates", "Section Name: $selectedSecionName | SectionID: $selectedSecionID | FacultyID: $userID")
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apiGetCohorts,
                null,
                { response ->
                    Log.d("Fetched Attendance", "Response: $response")
                    studentNames.clear()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val studentname = jsonObject.getString("Name")
                        studentNames.add(studentname)
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
                    Toast.makeText(this, "Attendance marked for this date!", Toast.LENGTH_SHORT).show()
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
            val apiUrl = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Faculty/Attendance/mark_new_attendance.php?AttendanceStatus=$status&Name=$name&Date=$selectedDate&SessionID=$selectedCourseSessionID&CourseID=$selectedCourseID&SectionID=$selectedSecionID"
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

    private fun fetchAttendanceDates(callback: (List<String>) -> Unit) {
        if (userType == "faculty"){
            val reqQueue: RequestQueue = Volley.newRequestQueue(this)
            val apiGetCohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Faculty/Attendance/fetch_attendance_dates.php?FacultyID=$userID&SessionID=$selectedCourseSessionID&SectionID=$selectedSecionID"
            Log.d("Fetched Dates", "URL: $apiGetCohorts")
            Log.d("Fetched Dates", "Section Name: $selectedSecionName | SectionID: $selectedSecionID | FacultyID: $userID")
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apiGetCohorts,
                null,
                { response ->
                    Log.d("Fetched Attendance", "Response: $response")
                    attendance_dates.clear()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val studentname = jsonObject.getString("Date")
                        attendance_dates.add(studentname)
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
            val apiGetCohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Faculty/Attendance/fetch_attendance_dates.php?FacultyID=$selectedFacultyID&SessionID=$selectedCourseSessionID&SectionID=$selectedSecionID"
            Log.d("Fetched Dates", "URL: $apiGetCohorts")
            Log.d("Fetched Dates", "Section Name: $selectedSecionName | SectionID: $selectedSecionID | FacultyID: $userID")
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apiGetCohorts,
                null,
                { response ->
                    Log.d("Fetched Attendance", "Response: $response")
                    attendance_dates.clear()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val studentname = jsonObject.getString("Date")
                        attendance_dates.add(studentname)
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
}