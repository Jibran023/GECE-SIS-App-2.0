package Faculty_Admin.Admin_Mapping

import Faculty_Admin.Announcement.AdminChooseAnnouncement
import Faculty_Admin.Announcement.CheckboxAdapter
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.R
import java.net.URLEncoder

class AdminMapping4 : AppCompatActivity() {
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

    private lateinit var select_students: TextView
    private lateinit var SectionName: TextView
    private lateinit var select_students_list: List<String>
    private lateinit var studentDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_mapping4)

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

        select_students = findViewById(R.id.select_students)
        SectionName = findViewById(R.id.enter_sectionName)

        select_students_list = emptyList()

        val backbtn = findViewById<ImageButton>(R.id.backbtn)
        backbtn.setOnClickListener {
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

        Log.d("LATESTINFO", "Cohort select: $selectedCohort | Course ID selected: $selectedCourseID | Course Name: $selectedCourseName | Instructor: $selectedCourseIDInstructor | Instructor ID: $selectedCourseFacultyID | Course Session: $selectedCourseSessionID | Session Description: $selectedCourseDescription ")

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, AdminMapping::class.java).apply {
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
        val apiGetStudents = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Announcements/Admin/fetch_students_by_cohort.php?cohorts=$selectedCohort"

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
        fetchStudents() { students ->
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

}