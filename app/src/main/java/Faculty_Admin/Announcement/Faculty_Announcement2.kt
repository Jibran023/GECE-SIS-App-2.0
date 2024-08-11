package Faculty_Admin.Announcement

import Faculty_Admin.Dashboards.AdminDashboard
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import Faculty_Admin.Dashboards.FacultyDashboard
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class Faculty_Announcement2 : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID : String
    private lateinit var recyclerView: RecyclerView
    private lateinit var announcementAdapter: AnnouncementAdapter
    private val announcementList = mutableListOf<Announcement>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_announcement2)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()

        Log.d("FacultyAnnounce2", "User Type is: $userType | User ID is: $userID")

        val backbtn = findViewById<ImageButton>(R.id.backbtn)
        backbtn.setOnClickListener {
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

        val add_button = findViewById<FloatingActionButton>(R.id.add_button)
        add_button.setOnClickListener {
            val intent = Intent(this, Faculty_Announcement::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        announcementAdapter = AnnouncementAdapter(announcementList)
        recyclerView.adapter = announcementAdapter

        fetchfacultyannouncements()

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

    private fun fetchfacultyannouncements(){
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val useridint = userID.toIntOrNull()?: 1
        val apigetcohorts = "http://192.168.18.55/geceapi/Faculty_Admin/Announcements/Faculty/fetchfacultyannouncements.php?id=$useridint"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apigetcohorts,
            null,
            { response ->
                Log.d("FACANNOUNCE", "Fetched JSON Data: $response")
                try {

                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val announcement = Announcement(
                            title = jsonObject.getString("Title"),
                            content = jsonObject.getString("Content"),
                            announcementBy = jsonObject.getString("Name"),
                            postedDate = jsonObject.getString("PostedDateTime"),
                            course = ""
                        )
                        announcementList.add(announcement)
                    }
                    announcementAdapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    e.printStackTrace()

                }
            },
            { error ->
                Log.e("FACANNOUNCE", "Error fetching the data: ${error.message}")
            }
        )
        reqQueue.add(jsonArrayRequest)
    }

}