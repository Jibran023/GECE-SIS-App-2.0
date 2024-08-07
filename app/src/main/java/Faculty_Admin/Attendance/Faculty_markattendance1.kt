package Faculty_Admin.Attendance

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gece_sisapp20.R

class faculty_markattendance1 : AppCompatActivity() {
    private var userType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_markattendance1)
        userType = intent.getStringExtra("USER_TYPE")

        val backtbtn = findViewById<ImageView>(R.id.backbtn)
        backtbtn.setOnClickListener {
            val intent = Intent(this, FacultyChooseAttendance::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // Retrieve class dates from previous screen , faculty_markattendance2.kt
//        val classDates = intent.getStringArrayListExtra("class_dates") ?: listOf()
//        val sectionsrecieved = intent.getStringArrayListExtra("sections") ?: listOf()


        // Initialize the Spinner
        val cohortSpinner: Spinner = findViewById(R.id.cohort_spinner)
        val sectionSpinner: Spinner = findViewById(R.id.semester_spinner)
        // Dummy sections data
//        val sections = arrayOf("Section A", "Section B", "Section C", "Section D")
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sectionsrecieved)

        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
//        sectionSpinner.adapter = adapter

        // Handle button click
        val proceedButton: Button = findViewById(R.id.nextbtn)
        proceedButton.setOnClickListener {
//            val selectedSection = sectionSpinner.selectedItem.toString()
            // Show selected section as a Toast message
//            Toast.makeText(this, "Selected Section: $selectedSection", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, faculty_markattendance2::class.java).apply {
//                putStringArrayListExtra("class_dates", ArrayList(classDates)) // Passing the class dates
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)


        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, FacultyChooseAttendance::class.java).apply {
            putExtra("USER_TYPE", userType)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}