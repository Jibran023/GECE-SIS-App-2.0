package Faculty_Admin.ProfileView

import Faculty_Admin.Dashboards.AdminDashboard
import Faculty_Admin.Dashboards.FacultyDashboard
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
import com.example.gece_sisapp20.R
import com.example.gece_sisapp20.UserCredentials


class Profile_Faculty : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID: String

    private lateinit var emailTextView: TextView
    private lateinit var rollnoTextView: TextView
    private lateinit var seatnoTextView: TextView // department
    private lateinit var nameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_faculty)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString() // Takes ID from the previous screen

        Log.d("Profilefaculty_Data", "User Type is: $userType | User ID is: $userID")

        emailTextView = findViewById(R.id.email) // setting up the textviews
        rollnoTextView = findViewById(R.id.rollno)
        seatnoTextView = findViewById(R.id.seatno)
        nameTextView = findViewById(R.id.username)

        fetchsuserdata()

        val dashboard_icon = findViewById<LinearLayout>(R.id.dashboard_icon)
        val inbox_icon = findViewById<LinearLayout>(R.id.inbox_icon)
        val logout_icon = findViewById<LinearLayout>(R.id.logout_id)

        inbox_icon.setOnClickListener {
            val intent = Intent(this, Messages::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

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
        finish()
    }

    private fun fetchsuserdata() {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val IDint = userID.toIntOrNull() ?: 1 // We know it is a studentid hence we haven't kept a check

        if (userType == "admin") {
            val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/ProfileView/usersprofileinfo.php?id=$IDint"
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apigetcohorts,
                null,
                { response ->
                    Log.d("UsersData", "Fetched JSON Data: $response")
                    try {
                        for (i in 0 until response.length()) {
                            val jsonObject = response.getJSONObject(i)
                            val name = jsonObject.getString("Name") // We retrieve users data
                            val email = jsonObject.getString("Email")
                            val role = jsonObject.getString("Role")
                            val department = jsonObject.getString("department")

                            emailTextView.text = "Email: $email"
                            rollnoTextView.text = "Role: $role"
                            seatnoTextView.text = "Department: $department"
                            nameTextView.text = "$name"

                            Log.d("UsersData", "Name: $name Email: $email Role: $role  Department: $department")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                { error ->
                    Log.e("UsersData", "Error fetching the data: ${error.message}")
                }
            )
            reqQueue.add(jsonArrayRequest)
        }

        else if (userType == "faculty") {
            val apigetfacultydata = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/ProfileView/facultyprofileinfo.php?id=$IDint"

            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apigetfacultydata,
                null,
                { response ->
                    Log.e("FacultyDataProfile", "Fetched JSON Data: $response")
                    try {
                        for (i in 0 until response.length()) {
                            val jsonObject = response.getJSONObject(i)
                            val name = jsonObject.getString("FacultyName") // Assuming 'id' is a string
                            val email = jsonObject.getString("Email")

                            emailTextView.text = "Email: $email"
                            nameTextView.text = "$name"

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                { error ->
                    Log.e("FacultyData", "Error fetching the data: ${error.message}")
                }
            )
            reqQueue.add(jsonArrayRequest)
        }
    }
}