package Faculty_Admin.Admin_Mapping

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageButton
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.R
import com.example.gece_sisapp20.SectionAdapter
import com.example.gece_sisapp20.SectionData
import com.google.android.material.floatingactionbutton.FloatingActionButton



class AdminMapping2 : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID: String
    private lateinit var selectedCohort: String
    private lateinit var selectedCourseID: String
    private var OfferedSections: ArrayList<SectionData> = arrayListOf()
    private lateinit var selectedCourseIDInstructor: String
    private lateinit var selectedCourseName: String
    private lateinit var sectionAdapter: SectionAdapter
    private lateinit var recyclerViewSections : RecyclerView
    private lateinit var selectedCourseFacultyID: String
    private lateinit var selectedCourseSessionID: String
    private lateinit var selectedCourseDescription: String
//    private lateinit var sectionID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_mapping2)

        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()
        selectedCohort = intent.getStringExtra("COHORT").toString()
        selectedCourseID = intent.getStringExtra("COURSE_ID").toString()
        selectedCourseIDInstructor = intent.getStringExtra("INSTRUCTOR").toString()
        selectedCourseName = intent.getStringExtra("COURSE_NAME").toString()
        selectedCourseFacultyID = intent.getStringExtra("FACULTY_ID").toString()
        selectedCourseSessionID = intent.getStringExtra("COURSE_SESSION").toString()
        selectedCourseDescription = intent.getStringExtra("COURSE_DESC").toString()
//        sectionID = intent.getStringExtra("SECTION_ID").toString()




        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            var intent = Intent(this, AdminMapping::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)

                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        val addSectionbtn = findViewById<FloatingActionButton>(R.id.add_button)
        addSectionbtn.setOnClickListener{
            var intent = Intent(this, AdminMapping4::class.java).apply {
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


        recyclerViewSections = findViewById(R.id.recyclerViewSections)

        recyclerViewSections.layoutManager = LinearLayoutManager(this)
        sectionAdapter = SectionAdapter(OfferedSections) // Initialize with empty list
        recyclerViewSections.adapter = sectionAdapter

        fetchOfferedCoursesSections() // Fetch sections when the activity starts

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, AdminMapping::class.java).apply {
            putExtra("USER_TYPE", userType)
            putExtra("USER_ID", userID)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }



    private fun fetchOfferedCoursesSections(){
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Admin/Mapping/fetch_offered_courses_sections.php?year=$selectedCohort&rollNumber=$selectedCourseID"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apigetcohorts,
            null,
            { response ->
                Log.d("FOSC", "Fetched Offered Courses Sections are: $response")
                try {
                    OfferedSections.clear() // Clearing previous courses and their IDs
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val courseName = jsonObject.getString("SectionName")
                        val sectionID = jsonObject.getString("SectionID")
                        val sectionData = SectionData(
                            sectionName = courseName,
                            sectionID = sectionID,
                            courseName = selectedCourseName,
                            professorName = selectedCourseIDInstructor,
                            userType = userType,
                            userID = userID,
                            selectedCohort = selectedCohort,
                            selectedCourseID = selectedCourseID,
                            selectedCourseFacultyID = selectedCourseFacultyID,
                            selectedCourseSessionID = selectedCourseSessionID,
                            selectedCourseDescription = selectedCourseDescription
                        )
                        OfferedSections.add(sectionData)
                        Log.d("FOSC", "Section: $courseName | Course: $selectedCourseName | Instructor: $selectedCourseIDInstructor")
                    }
                    recyclerViewSections.adapter?.notifyDataSetChanged()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                Log.e("StudentsDataFetchCourses", "Error fetching the data in FetchCourses: ${error.message}")
            }
        )
        reqQueue.add(jsonArrayRequest)
    }

    private fun saveToSharedPreferences() {
        val sharedPreferences = getSharedPreferences("AdminMappingPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("SELECTED_COHORT", selectedCohort)
        editor.putString("SELECTED_COURSE_ID", selectedCourseID)
        editor.apply() // or editor.commit() for synchronous save
    }

    private fun retrieveFromSharedPreferences() {
        val sharedPreferences = getSharedPreferences("AdminMappingPrefs", MODE_PRIVATE)
        selectedCohort = sharedPreferences.getString("SELECTED_COHORT", "") ?: ""
        selectedCourseID = sharedPreferences.getString("SELECTED_COURSE_ID", "") ?: ""
    }



}