package Student.ProfileView

import Faculty_Admin.Dashboards.AdminDashboard
import Faculty_Admin.Dashboards.FacultyDashboard
import Student.Dashboard.StudentDashboard
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.OtherDashboard
import com.example.gece_sisapp20.R



class ProfilePicture : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID: String

    private lateinit var emailTextView: TextView
    private lateinit var rollnoTextView: TextView
    private lateinit var yearaddTextView: TextView
    private lateinit var cohortTextView: TextView
    private lateinit var seatnoTextView: TextView
    private lateinit var nameTextView: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_picture)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString() // Takes studentID from the previous screen

        val dashboard_icon = findViewById<LinearLayout>(R.id.dashboard_icon)
        val logout_icon = findViewById<LinearLayout>(R.id.logout_id)

        emailTextView = findViewById(R.id.email)
        rollnoTextView = findViewById(R.id.rollno)
        yearaddTextView = findViewById(R.id.year)
        cohortTextView = findViewById(R.id.cohort)
        seatnoTextView = findViewById(R.id.seatno)
        nameTextView = findViewById(R.id.username)

        fetchstudentsdata()

//        email.text = "Email: $emailTextView
        Log.d("StudentDetails", "StudentID on Profile page is: $userID")

        dashboard_icon.setOnClickListener {
            if (userType == "faculty") {
                val intent = Intent(this, FacultyDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                }
                startActivity(intent)
            } else if (userType == "admin") {
                val intent = Intent(this, AdminDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            else if (userType == "student") {
                val intent = Intent(this, StudentDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            else if (userType == "other"){
                val intent = Intent(this, OtherDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        }

        logout_icon.setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (userType == "faculty") {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FacultyDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        } else if (userType == "admin") {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AdminDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
        else if (userType == "student") {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, StudentDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
        else if (userType == "other"){
            val intent = Intent(this, OtherDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
        finish()
    }

    private fun fetchstudentsdata(){
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        if (userType == "student"){
            val studentIDint = userID.toIntOrNull() ?: 1 // We know it is a studentid hence we haven't kept a check here
            val apigetcohorts = "http://192.168.18.55/geceapi/Student/ProfileView/studentsprofileinfo.php?id=$studentIDint"

            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apigetcohorts,
                null,
                { response ->
                    Log.e("StudentsFullData", "$response")
                    try {
                        if (response.length() > 0) {
                            val jsonObject = response.getJSONObject(0)
                            val email = jsonObject.getString("Email")
                            val rollno = jsonObject.getString("RollNumber")
                            val year = jsonObject.getString("YearAdmission")
                            val cohort = jsonObject.getString("cohort")
                            val seatno = jsonObject.getString("SeatNumber")
                            val name = jsonObject.getString("Name")

                            // Set the TextViews with the fetched data
                            emailTextView.text = "Email: $email"
                            rollnoTextView.text = "Roll Number: $rollno"
                            yearaddTextView.text = "Year of Admission: $year"
                            cohortTextView.text = "Cohort: $cohort"
                            seatnoTextView.text = "Seat Number: $seatno"
                            nameTextView.text = "$name"
                        } else {
                            Log.d("StudentsDataProfile", "No data found")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                { error ->
                    Log.e("StudentsDataProfile", "Error fetching the data: ${error.message}")
                }
            )

            reqQueue.add(jsonArrayRequest)
        }

        else if (userType == "other"){
            val studentIDint = userID.toIntOrNull() ?: 1 // We know it is a studentid hence we haven't kept a check here
            val apigetcohorts = "http://192.168.18.55/geceapi/otherusersinfo.php?id=$studentIDint"

            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apigetcohorts,
                null,
                { response ->
                    Log.e("StudentsFullData", "$response")
                    try {
                        if (response.length() > 0) {
                            val jsonObject = response.getJSONObject(0)
                            val email = jsonObject.getString("Email")
                            val rollno = jsonObject.getString("Role")
                            val year = jsonObject.getString("department")
                            val name = jsonObject.getString("Name")

                            // Set the TextViews with the fetched data
                            emailTextView.text = "Email: $email"
                            rollnoTextView.text = "Role: $rollno"
                            yearaddTextView.text = "Department: $year"
                            cohortTextView.text = ""
                            seatnoTextView.text = ""
                            nameTextView.text = "$name"
                        } else {
                            Log.d("StudentsDataProfile", "No data found")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                { error ->
                    Log.e("StudentsDataProfile", "Error fetching the data: ${error.message}")
                }
            )

            reqQueue.add(jsonArrayRequest)
        }

    }
}