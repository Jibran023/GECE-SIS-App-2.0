package Faculty_Admin.Admin_Mapping

import Faculty_Admin.Announcement.CheckboxAdapter
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.R
import com.example.gece_sisapp20.StudentAdapter2
import org.json.JSONObject
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


    private lateinit var select_students: TextView
    private lateinit var SectionName: EditText
    private lateinit var select_students_list_to_add: List<String>
    private lateinit var studentsaddDialog: Dialog
    private lateinit var createSection: Button

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

        val backbtn = findViewById<ImageButton>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, AdminMapping2::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                putExtra("COHORT", selectedCohort)
                putExtra("COURSE_ID", selectedCourseID)
                putExtra("COURSE_NAME", selectedCourseName)
                putExtra("INSTRUCTOR", selectedCourseIDInstructor)
                putExtra("FACULTY_ID", selectedCourseFacultyID)
                putExtra("COURSE_SESSION", selectedCourseSessionID)
                putExtra("COURSE_DESC", selectedCourseDescription)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        SectionName = findViewById(R.id.enter_sectionName)
        select_students_list_to_add = emptyList()

        select_students = findViewById(R.id.select_students)
        select_students.setOnClickListener{
            showStudentDialog()
        }

        createSection = findViewById(R.id.createbtn)
        createSection.setOnClickListener{
            val sectionName = SectionName.text.toString()
            val studentstoadd = select_students.text.toString()

            if (sectionName.isBlank()) {
                Toast.makeText(this, "Please enter a section name!", Toast.LENGTH_SHORT).show()
            } else {
                // Show confirmation dialog before creating the section
                if (studentstoadd.isBlank()){
                    Toast.makeText(this, "Please select a student!", Toast.LENGTH_SHORT).show()
                }
                else{
                    showCreateSectionConfirmationDialog(sectionName)
                }

            }
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
            putExtra("COURSE_NAME", selectedCourseName)
            putExtra("INSTRUCTOR", selectedCourseIDInstructor)
            putExtra("FACULTY_ID", selectedCourseFacultyID)
            putExtra("COURSE_SESSION", selectedCourseSessionID)
            putExtra("COURSE_DESC", selectedCourseDescription)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun fetchStudentstoadd(callback: (List<String>) -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apiGetStudents = "${LoginScreen.BASE_URL}/geceapi/Admin/Mapping/fetch_other_section_students.php?year=$selectedCourseSessionID&rollNumber=$selectedCourseID"
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
        fetchStudentstoadd { students ->
            studentsaddDialog = createDialogWithCheckboxestoadd(
                this, "Select Students", students
            ) { selectedStudents ->
                select_students_list_to_add = selectedStudents.toMutableList()
                select_students.text = selectedStudents.toString()
                Log.d("ShowStudentAddDialog", "Selected Students: ${selectedStudents.joinToString(", ")}")
            }
            studentsaddDialog.show()

        }
    }

    private fun createDialogWithCheckboxestoadd(context: Context, title: String, items: List<String>, onItemsSelected: (selectedItems: List<String>) -> Unit): Dialog {
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
            if (selectedItems.isEmpty()) {
                Toast.makeText(context, "No students selected!", Toast.LENGTH_SHORT).show()
            } else {
                // Show the confirmation dialog
                val confirmationDialog = Dialog(context)
                confirmationDialog.setContentView(R.layout.dialog_confirmation) // You need to create this layout

                val scrollView = confirmationDialog.findViewById<ScrollView>(R.id.confirmation_scroll_view)
                val confirmText = confirmationDialog.findViewById<TextView>(R.id.confirmation_text)

                // Set the confirmation message with selected students
                val studentsListText = selectedItems.joinToString(separator = "\n")
                confirmText.text = "Are you sure you want to add these students?\n\n$studentsListText"

                // Adjust ScrollView dimensions
                val params = scrollView.layoutParams
                params.height = context.resources.getDimensionPixelSize(R.dimen.confirmation_dialog_height)
                params.width = context.resources.getDimensionPixelSize(R.dimen.confirmation_dialog_width)
                scrollView.layoutParams = params


                val yesButton = confirmationDialog.findViewById<Button>(R.id.yes_button)
                val noButton = confirmationDialog.findViewById<Button>(R.id.no_button)

                yesButton.setOnClickListener {
                    // Handle the user's confirmation
                    onItemsSelected(selectedItems)

                    // Call function to set create students and add students in it

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
        return dialog
    }

    private fun createSection(sectionName: String, callback: (String?) -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apiCreateSection = "${LoginScreen.BASE_URL}/geceapi/Admin/Mapping/create_new_section.php?CourseID=$selectedCourseID&SectionName=${URLEncoder.encode(sectionName, "UTF-8")}"

        // Create a request to get a JSON object instead of a JSON array, since the response is a single object
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            apiCreateSection,
            null,
            { response ->
                try {
                    // Check if the response contains a success status
                    val status = response.getString("status")
                    if (status == "success") {
                        // Get the SectionID from the response
                        val sectionID = response.getString("SectionID")
                        callback(sectionID) // Pass the SectionID to the callback
                    } else {
                        // Log warning message from the server
                        val message = response.getString("message")
                        Log.w("CreateSection", "Warning: $message")
                        callback(null) // Pass null to the callback to indicate failure
                    }
                } catch (e: Exception) {
                    Log.e("CreateSection", "Error parsing response: ${e.message}")
                    callback(null) // Pass null to the callback to indicate failure
                }
            },
            { error ->
                Log.e("CreateSection", "Error creating section: ${error.message}")
                callback(null) // Pass null to the callback to indicate failure
            }
        )

        // Add the request to the request queue
        reqQueue.add(jsonObjectRequest)
    }

    private fun addSectionToFacultyCourses(sectionID: String, callback: (Boolean) -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apiAddSectionToFacultyCourses = "${LoginScreen.BASE_URL}/geceapi/Admin/Mapping/add_section_for_faculty.php?FacultyID=$selectedCourseFacultyID&CourseID=$selectedCourseID&SessionID=$selectedCourseSessionID&SectionID=$sectionID"

        val stringRequest = StringRequest(
            Request.Method.GET,
            apiAddSectionToFacultyCourses,
            { response ->
                val jsonObject = JSONObject(response)
                val status = jsonObject.getString("status")
                callback(status == "success")
            },
            { error ->
                Log.e("AddSectionToFaculty", "Error: ${error.message}")
                callback(false)
            }
        )

        reqQueue.add(stringRequest)
    }

    private fun updateStudentsWithNewSection(sectionID: String, callback: (Boolean) -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val studentNames = URLEncoder.encode(select_students_list_to_add.joinToString(","), "UTF-8")
        val apiUpdateStudents = "${LoginScreen.BASE_URL}/geceapi/Admin/Mapping/add_students_to_new_section.php?SessionID=$selectedCourseSessionID&CourseID=$selectedCourseID&SectionID=$sectionID&StudentNames=$studentNames"

        val stringRequest = StringRequest(
            Request.Method.GET,
            apiUpdateStudents,
            { response ->
                val jsonObject = JSONObject(response)
                val status = jsonObject.getString("status")
                callback(status == "success")
            },
            { error ->
                Log.e("UpdateStudents", "Error: ${error.message}")
                callback(false)
            }
        )

        reqQueue.add(stringRequest)
    }

    @SuppressLint("SetTextI18n")
    private fun showCreateSectionConfirmationDialog(sectionName: String) {
        // Create a confirmation dialog
        val confirmationDialog = Dialog(this)
        confirmationDialog.setContentView(R.layout.dialog_confirmation)

        val confirmText = confirmationDialog.findViewById<TextView>(R.id.confirmation_text)
        confirmText.text = "Are you sure you want to create the section: $sectionName?"

        val yesButton = confirmationDialog.findViewById<Button>(R.id.yes_button)
        val noButton = confirmationDialog.findViewById<Button>(R.id.no_button)

        yesButton.setOnClickListener {
//          Proceed with section creation
            createSection(sectionName) { sectionID ->
                if (sectionID != null)
                {
                    addSectionToFacultyCourses(sectionID){success ->
                        if(success)
                        {
                            updateStudentsWithNewSection(sectionID) {updatesuccess ->
                                if (updatesuccess){
                                    Toast.makeText(this, "Section created and students updated successfully with ID: $sectionID", Toast.LENGTH_LONG).show()
                                    // Clear the TextView after successful operations
                                    select_students.text = ""
                                }
                                else {
                                    Toast.makeText(this, "Section created but failed to update students", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        else {
                            Toast.makeText(this, "Failed to add section to faculty courses", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Failed to create section", Toast.LENGTH_LONG).show()
                }
            }

            confirmationDialog.dismiss() // Close the confirmation dialog
        }

        noButton.setOnClickListener {
            confirmationDialog.dismiss() // Close the confirmation dialog without creating the section
        }

        confirmationDialog.show() // Display the confirmation dialog
    }

}