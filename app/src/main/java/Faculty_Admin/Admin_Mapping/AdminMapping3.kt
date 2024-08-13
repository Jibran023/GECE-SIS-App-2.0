package Faculty_Admin.Admin_Mapping

import Faculty_Admin.Announcement.CheckboxAdapter
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.R
import com.example.gece_sisapp20.StudentAdapter2
import java.net.URLEncoder


class AdminMapping3 : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID: String
    private lateinit var selectedCohort: String
    private lateinit var selectedCourseID: String
    private lateinit var selectedCourseIDInstructor: String
    private lateinit var selectedCourseName: String
    private lateinit var selectedCourseFacultyID: String
    private lateinit var selectedCourseSessionID: String
    private lateinit var selectedCourseDescription: String
    private lateinit var sectionName: String
    private lateinit var sectionID: String

    private lateinit var addstudents_btn: Button
    private lateinit var removestudents_btn: Button
    private lateinit var delete_section_btn: Button

    private lateinit var studentsaddDialog: Dialog
    private lateinit var studentsremoveDialog: Dialog
    private lateinit var select_students_list: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_mapping3)

        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()
        selectedCohort = intent.getStringExtra("COHORT").toString()
        selectedCourseID = intent.getStringExtra("COURSE_ID").toString()
        selectedCourseIDInstructor = intent.getStringExtra("INSTRUCTOR").toString()
        selectedCourseName = intent.getStringExtra("COURSE_NAME").toString()
        selectedCourseFacultyID = intent.getStringExtra("FACULTY_ID").toString()
        selectedCourseSessionID = intent.getStringExtra("COURSE_SESSION").toString()
        selectedCourseDescription = intent.getStringExtra("COURSE_DESC").toString()
        sectionName = intent.getStringExtra("SECTION_NAME").toString()
        sectionID = intent.getStringExtra("SECTION_ID").toString()

        Log.d("LATESTINFO", "Cohort select: $selectedCohort | Course ID selected: $selectedCourseID | Course Name: $selectedCourseName | Instructor: $selectedCourseIDInstructor | Instructor ID: $selectedCourseFacultyID | Course Session: $selectedCourseSessionID | Session Description: $selectedCourseDescription | Section Name: $sectionName | Section ID: $sectionID")

        // Find views by ID
        val studentCountTextView : TextView = findViewById(R.id.studentCountTextView)


        // Back button functionality
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            var intent = Intent(this, AdminMapping2::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                putExtra("COHORT", selectedCohort)
                putExtra("COURSE_ID", selectedCourseID)
                putExtra("SECTION_NAME", sectionName)
                putExtra("COURSE_NAME", selectedCourseName)
                putExtra("INSTRUCTOR", selectedCourseIDInstructor)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // Sample data for students
        val students = listOf("John Doe", "Jane Smith", "Emily Johnson") // Replace with actual student data
        studentCountTextView.text = "Number of students mapped: ${students.size}"

        // Set up the RecyclerView
        val recyclerViewStudents : RecyclerView = findViewById(R.id.recyclerViewStudents)
        // Fetch students and update the RecyclerView
        fetchStudents { students ->
            // Update the student count text
            studentCountTextView.text = "Number of students mapped: ${students.size}"

            // Set up the RecyclerView with fetched students
            recyclerViewStudents.layoutManager = LinearLayoutManager(this)
            recyclerViewStudents.adapter = StudentAdapter2(students)
        }

        addstudents_btn = findViewById(R.id.nextbutton)
        addstudents_btn.setOnClickListener{
            showStudentAddDialog()
        }
        removestudents_btn = findViewById(R.id.move)
        removestudents_btn.setOnClickListener{
            showStudentDialog()
        }
        delete_section_btn = findViewById(R.id.remove)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, AdminMapping2::class.java).apply {
            putExtra("USER_TYPE", userType)
            putExtra("USER_ID", userID)
            putExtra("COHORT", selectedCohort)
            putExtra("COURSE_ID", selectedCourseID)
            putExtra("SECTION_NAME", sectionName)
            putExtra("COURSE_NAME", selectedCourseName)
            putExtra("INSTRUCTOR", selectedCourseIDInstructor)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun fetchStudents(callback: (List<String>) -> Unit) {
            val reqQueue: RequestQueue = Volley.newRequestQueue(this)
            val apiGetStudents = "${LoginScreen.BASE_URL}/geceapi/Admin/Mapping/fetch_section_students.php?year=$selectedCourseSessionID&rollNumber=$selectedCourseID&id=$sectionID"

            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apiGetStudents,
                null,
                { response ->
                    val students = mutableListOf<String>()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val student = jsonObject.getString("Name")
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

    private fun fetchStudentstoadd(callback: (List<String>) -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apiGetStudents = "${LoginScreen.BASE_URL}/geceapi/Admin/Mapping/fetch_other_section_students.php?year=$selectedCourseSessionID&rollNumber=$selectedCourseID&id=$sectionID"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apiGetStudents,
            null,
            { response ->
                val students = mutableListOf<String>()
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val student = jsonObject.getString("Name")
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

    private fun showStudentDialog() {
            fetchStudents { students ->
                studentsremoveDialog = createDialogWithCheckboxes(
                    this, "Select Students", students
                ) { selectedStudents ->
                    select_students_list = selectedStudents
                    Log.d("ShowStudentDialog", "Selected Students: ${selectedStudents.joinToString(", ")}")
//                    select_students.text = selectedStudents.joinToString(", ")
                }
                studentsremoveDialog.show()

        }
    }

    private fun showStudentAddDialog() {
        fetchStudentstoadd { students ->
            studentsaddDialog = createDialogWithCheckboxestoadd(
                this, "Select Students", students
            ) { selectedStudents ->
                select_students_list = selectedStudents
                Log.d("ShowStudentDialog", "Selected Students: ${selectedStudents.joinToString(", ")}")
//                    select_students.text = selectedStudents.joinToString(", ")
            }
            studentsaddDialog.show()

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
            // Show the confirmation dialog
            val confirmationDialog = Dialog(context)
            confirmationDialog.setContentView(R.layout.dialog_confirmation) // You need to create this layout

            val confirmText = confirmationDialog.findViewById<TextView>(R.id.confirmation_text)
            confirmText.text = "Are you sure you want to apply these changes?"

            val yesButton = confirmationDialog.findViewById<Button>(R.id.yes_button)
            val noButton = confirmationDialog.findViewById<Button>(R.id.no_button)

            yesButton.setOnClickListener {
                // Handle the user's confirmation
                if (selectedItems.contains("All")) {
                    selectedItems.clear()
                    selectedItems.addAll(items.filter { it != "All" })
                }
                onItemsSelected(selectedItems)
                confirmationDialog.dismiss() // Close the confirmation dialog
                dialog.dismiss() // Close the main dialog
            }

            noButton.setOnClickListener {
                // Dismiss the confirmation dialog without making any changes
                confirmationDialog.dismiss()
            }

            confirmationDialog.show()
        }

        return dialog
    }

    private fun createDialogWithCheckboxestoadd(
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
            // Show the confirmation dialog
            val confirmationDialog = Dialog(context)
            confirmationDialog.setContentView(R.layout.dialog_confirmation) // You need to create this layout

            val confirmText = confirmationDialog.findViewById<TextView>(R.id.confirmation_text)
            confirmText.text = "Are you sure you want to apply these changes?"

            val yesButton = confirmationDialog.findViewById<Button>(R.id.yes_button)
            val noButton = confirmationDialog.findViewById<Button>(R.id.no_button)

            yesButton.setOnClickListener {
                // Handle the user's confirmation
                if (selectedItems.contains("All")) {
                    selectedItems.clear()
                    selectedItems.addAll(items.filter { it != "All" })
                }
                onItemsSelected(selectedItems)
                confirmationDialog.dismiss() // Close the confirmation dialog
                dialog.dismiss() // Close the main dialog
            }

            noButton.setOnClickListener {
                // Dismiss the confirmation dialog without making any changes
                confirmationDialog.dismiss()
            }

            confirmationDialog.show()
        }

        return dialog
    }

}