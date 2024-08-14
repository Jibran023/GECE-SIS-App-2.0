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
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
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
import org.json.JSONException
import org.json.JSONObject
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
    private var select_students_list_to_remove = mutableListOf<String>()
    private var select_students_list_to_add = mutableListOf<String>()

    private lateinit var recyclerViewStudents : RecyclerView
    private lateinit var studentCountTextView: TextView

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
        studentCountTextView = findViewById(R.id.studentCountTextView)


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
                putExtra("COURSE_SESSION", selectedCourseSessionID)
//                putExtra("SECTION_ID", sectionID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // Sample data for students
        val students = listOf("John Doe", "Jane Smith", "Emily Johnson") // Replace with actual student data
        studentCountTextView.text = "Number of students mapped: ${students.size}"

        // Set up the RecyclerView
        recyclerViewStudents = findViewById(R.id.recyclerViewStudents)
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
            showStudentRemoveDialog()
        }

        delete_section_btn = findViewById(R.id.remove)
        delete_section_btn.setOnClickListener{
            showDeleteSectionConfirmationDialog()
        }

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
//            putExtra("SECTION_ID", sectionID)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun fetchStudents(callback: (List<String>) -> Unit) {
            val reqQueue: RequestQueue = Volley.newRequestQueue(this)
            val apiGetStudents = "${LoginScreen.BASE_URL}/geceapi/Admin/Mapping/fetch_section_students.php?year=$selectedCourseSessionID&rollNumber=$selectedCourseID&id=$sectionID"
//            Toast.makeText(this, "Selected Section: $sectionName | ID: $sectionName", Toast.LENGTH_SHORT).show()
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

    private fun showStudentRemoveDialog() {
            fetchStudents { students ->
                studentsremoveDialog = createDialogWithCheckboxestoremove(
                    this, "Select Students", students
                ) { selectedStudents ->
                    select_students_list_to_remove =
                        selectedStudents.toMutableList() // saving the selected students

                    // Log the selected students
                    Log.d("ShowStudentRemoveDialog", "Selected Students: ${selectedStudents.joinToString(", ")}")
                }
                studentsremoveDialog.show()

        }
    }

    private fun showStudentAddDialog() {
        fetchStudentstoadd { students ->
            studentsaddDialog = createDialogWithCheckboxestoadd(
                this, "Select Students", students
            ) { selectedStudents ->
                select_students_list_to_add = selectedStudents.toMutableList()

                Log.d("ShowStudentAddDialog", "Selected Students: ${selectedStudents.joinToString(", ")}")
            }
            studentsaddDialog.show()

        }
    }

    private fun removeStudentsFromSection(onSuccess: () -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        // Construct the URL with parameters
        val studentNames = URLEncoder.encode(select_students_list_to_remove.joinToString(","), "UTF-8")
        val apiRemoveStudents = "${LoginScreen.BASE_URL}/geceapi/Admin/Mapping/remove_students_from_section.php?SessionID=$selectedCourseSessionID&CourseID=$selectedCourseID&SectionID=$sectionID&StudentNames=$studentNames"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            apiRemoveStudents,
            null,
            { response ->
                try {
                    Log.d("ReSpO", "Response: $response")
                    // Check if the response is a JSONObject and handle it
                    val status = response.optString("status")
                    val message = response.optString("message")
                    if (status == "success") {
                        Log.d("RemoveStudents", "Successfully removed students")
                        // Optionally, show a success message to the user
                        onSuccess() // Call the onSuccess callback to fetch and update students
                    } else {
                        Log.e("RemoveStudents", "Error removing students: $message")
                        // Optionally, show an error message to the user
                    }
                } catch (e: JSONException) {
                    Log.e("RemoveStudents", "Error parsing response: ${e.message}")
                }
            },
            { error ->
                Log.e("RemoveStudents", "Error removing students: ${error.message}")
            }
        )
        reqQueue.add(jsonObjectRequest)
    }

    private fun createDialogWithCheckboxestoremove(context: Context, title: String, items: List<String>, onItemsSelected: (selectedItems: List<String>) -> Unit): Dialog {
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
                confirmText.text = "Are you sure you want to remove these students?\n\n$studentsListText"

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

                    removeStudentsFromSection {
                        // Fetch students and update the RecyclerView after removal
                        fetchStudents { updatedStudents ->
                            // Update the student count text
                            studentCountTextView.text = "Number of students mapped: ${updatedStudents.size}"

                            // Set up the RecyclerView with fetched students
                            recyclerViewStudents.layoutManager = LinearLayoutManager(this)
                            recyclerViewStudents.adapter = StudentAdapter2(updatedStudents)
                        }
                    }

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

    private fun addStudentstoThisSection(onSuccess: () -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        // Construct the URL with parameters
        val studentNames = URLEncoder.encode(select_students_list_to_add.joinToString(","), "UTF-8")
        val apiRemoveStudents = "${LoginScreen.BASE_URL}/geceapi/Admin/Mapping/add_students_to_section.php?SessionID=$selectedCourseSessionID&CourseID=$selectedCourseID&SectionID=$sectionID&StudentNames=$studentNames"

        Log.d("apiRemoveStds", "URL: $apiRemoveStudents")

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            apiRemoveStudents,
            null,
            { response ->
                try {
                    Log.d("ReSpO", "Response: $response")
                    // Check if the response is a JSONObject and handle it
                    val status = response.optString("status")
                    val message = response.optString("message")

                    if (status == "success") {
                        Log.d("RemoveStudents", "Successfully removed students")
                        // Optionally, show a success message to the user
                        onSuccess() // Call the onSuccess callback to fetch and update students
                    } else {
                        Log.e("RemoveStudents", "Error removing students: $message")
                        // Optionally, show an error message to the user
                    }
                } catch (e: JSONException) {
                    Log.e("RemoveStudents", "Error parsing response: ${e.message}")
                }
            },
            { error ->
                Log.e("RemoveStudents", "Error removing students: ${error.message}")
            }
        )
        reqQueue.add(jsonObjectRequest)
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

                    addStudentstoThisSection {
                        // Fetch students and update the RecyclerView after removal
                        fetchStudents { updatedStudents ->
                            // Update the student count text
                            studentCountTextView.text = "Number of students mapped: ${updatedStudents.size}"

                            // Set up the RecyclerView with fetched students
                            recyclerViewStudents.layoutManager = LinearLayoutManager(this)
                            recyclerViewStudents.adapter = StudentAdapter2(updatedStudents)
                        }
                    }

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

    private fun DeleteSection(onSuccess: () -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        // Construct the URL with parameters
        val studentNames = URLEncoder.encode(select_students_list_to_add.joinToString(","), "UTF-8")
        val apiRemoveStudents = "${LoginScreen.BASE_URL}/geceapi/Admin/Mapping/add_students_to_section.php?SessionID=$selectedCourseSessionID&CourseID=$selectedCourseID&SectionID=$sectionID&StudentNames=$studentNames"

        Log.d("apiRemoveStds", "URL: $apiRemoveStudents")

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            apiRemoveStudents,
            null,
            { response ->
                try {
                    Log.d("ReSpO", "Response: $response")
                    // Check if the response is a JSONObject and handle it
                    val status = response.optString("status")
                    val message = response.optString("message")

                    if (status == "success") {
                        Log.d("RemoveStudents", "Successfully removed students")
                        // Optionally, show a success message to the user
                        onSuccess() // Call the onSuccess callback to fetch and update students
                    } else {
                        Log.e("RemoveStudents", "Error removing students: $message")
                        // Optionally, show an error message to the user
                    }
                } catch (e: JSONException) {
                    Log.e("RemoveStudents", "Error parsing response: ${e.message}")
                }
            },
            { error ->
                Log.e("RemoveStudents", "Error removing students: ${error.message}")
            }
        )
        reqQueue.add(jsonObjectRequest)
    }

    private fun showDeleteSectionConfirmationDialog() {
        val confirmationDialog = Dialog(this)
        confirmationDialog.setContentView(R.layout.dialog_confirmation) // Use the layout you created for confirmation

        val scrollView = confirmationDialog.findViewById<ScrollView>(R.id.confirmation_scroll_view)
        val confirmText = confirmationDialog.findViewById<TextView>(R.id.confirmation_text)
        confirmText.text = "Are you sure you want to delete this section and remove all students?"

        val yesButton = confirmationDialog.findViewById<Button>(R.id.yes_button)
        val noButton = confirmationDialog.findViewById<Button>(R.id.no_button)

        yesButton.setOnClickListener {
            // Proceed with deletion
//            deleteSection {
//                // Callback after deletion is complete
//                Toast.makeText(this, "Section deleted successfully", Toast.LENGTH_SHORT).show()
//            }
            confirmationDialog.dismiss() // Close the confirmation dialog
        }

        noButton.setOnClickListener {
            confirmationDialog.dismiss() // Dismiss the confirmation dialog without making any changes
        }

        confirmationDialog.show()
    }

    private fun deleteSection(onSuccess: () -> Unit) {
        // First, remove all students from the section
        removeStudentsFromSection {
            // After removing students, update the facultycourses table
            updateFacultyCourses {
                // After updating facultycourses, delete the section
                deleteSectionFromTable {
                    onSuccess() // Call the onSuccess callback to notify that the deletion process is complete
                }
            }
        }
    }

    private fun updateFacultyCourses(onSuccess: () -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apiUpdateFacultyCourses = "${LoginScreen.BASE_URL}/geceapi/Admin/Mapping/remove_faculty_section.php?FacultyID=$selectedCourseFacultyID&CourseID=$selectedCourseID&SessionID=$selectedCourseSessionID"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            apiUpdateFacultyCourses,
            null,
            { response ->
                try {
                    Log.d("UpdateFacultyCourses", "Response: $response")
                    val status = response.optString("status")
                    val message = response.optString("message")
                    if (status == "success") {
                        Log.d("UpdateFacultyCourses", "Successfully updated faculty courses")
                        onSuccess() // Call the onSuccess callback to delete the section
                    } else {
                        Log.e("UpdateFacultyCourses", "Error updating faculty courses: $message")
                    }
                } catch (e: JSONException) {
                    Log.e("UpdateFacultyCourses", "Error parsing response: ${e.message}")
                }
            },
            { error ->
                Log.e("UpdateFacultyCourses", "Error updating faculty courses: ${error.message}")
            }
        )
        reqQueue.add(jsonObjectRequest)
    }

    private fun deleteSectionFromTable(onSuccess: () -> Unit) {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apiDeleteSection = "${LoginScreen.BASE_URL}/geceapi/Admin/Mapping/delete_this_section.php?SectionID=$sectionID"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            apiDeleteSection,
            null,
            { response ->
                try {
                    Log.d("DeleteSection", "Response: $response")
                    val status = response.optString("status")
                    val message = response.optString("message")
                    if (status == "success") {
                        Log.d("DeleteSection", "Successfully deleted section")
                        onSuccess() // Notify that the deletion process is complete
                    } else {
                        Log.e("DeleteSection", "Error deleting section: $message")
                    }
                } catch (e: JSONException) {
                    Log.e("DeleteSection", "Error parsing response: ${e.message}")
                }
            },
            { error ->
                Log.e("DeleteSection", "Error deleting section: ${error.message}")
            }
        )
        reqQueue.add(jsonObjectRequest)
    }


}