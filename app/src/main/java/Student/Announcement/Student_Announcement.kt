package Student.Announcement

import Faculty_Admin.Announcement.Announcement
import Faculty_Admin.Announcement.AnnouncementAdapter
import Faculty_Admin.Dashboards.AdminDashboard
import Faculty_Admin.Dashboards.FacultyDashboard
import Student.Dashboard.StudentDashboard
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.OtherDashboard
import com.example.gece_sisapp20.R


class Student_Announcement : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID : String
    private lateinit var recyclerView: RecyclerView
    private lateinit var announcementAdapter: AnnouncementAdapter
    private val announcementList = mutableListOf<Announcement>()
    private var rollno: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_announcement)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()

        Log.d("FacultyAnnounce2", "User Type is: $userType | User ID is: $userID")

        fetchstudentrollenumber()

        val backbtn = findViewById<ImageButton>(R.id.announcements_backbutton)
        backbtn.setOnClickListener {
            if (userType == "student"){
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

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        announcementAdapter = AnnouncementAdapter(announcementList)
        recyclerView.adapter = announcementAdapter




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

    private fun fetchstudentrollenumber(){
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val studentIDint = userID.toIntOrNull()?: 1

        if (userType== "student"){
            val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Student/Courses/studentsrole.php?id=$studentIDint"

            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apigetcohorts,
                null,
                { response ->
                    Log.d("StudentsData", "Fetched JSON Data: $response")
                    try {
                        for (i in 0 until response.length()) {
                            val jsonObject = response.getJSONObject(i)
                            val rolenumber = jsonObject.getString("RollNumber") // Assuming 'id' is a string

                            rollno = rolenumber

                            Log.d("RollNOP", "Fetched JSON Data: $rollno")
                        }
                        fetchstudentannouncementsfromadmin()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                { error ->
                    Log.e("StudentsData", "Error fetching the data in FetchRollno: ${error.message}")
                }
            )
            reqQueue.add(jsonArrayRequest)
        }
        else if (userType == "other")
        {
            fetchstudentannouncementsfromadmin() // We call the function only because we already have the user id
        }


    }

    private fun fetchstudentannouncementsfromadmin(){
        if (userType == "student"){
            if (rollno == null) {
                Log.e("STDANNOUNCE2", "Roll number is null, cannot fetch announcements.")
                return
            }
            val reqQueue: RequestQueue = Volley.newRequestQueue(this)
            val rollnoint = rollno?.toIntOrNull() ?: 1
            Log.d("ROLLLO", "Roll no is : $rollnoint")
            val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Announcements/Students/fetchstudentannouncementsbyboth.php?id=$rollnoint"
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apigetcohorts,
                null,
                { response ->
                    Log.d("STDANNOUNCE2", "Fetched JSON Data: $response")
                    try {

                        for (i in 0 until response.length()) {
                            val jsonObject = response.getJSONObject(i)
                            val announcement = Announcement(
                                title = jsonObject.getString("Title"),
                                content = jsonObject.getString("Content"),
                                announcementBy = jsonObject.getString("Name"),
                                postedDate = jsonObject.getString("PostedDateTime"),
                                course = jsonObject.getString("Course")
                            )
                            announcementList.add(announcement)
                        }
//                        fetchstudentannouncementsfromfaculty()
                        announcementAdapter.notifyDataSetChanged()
                    } catch (e: Exception) {
                        e.printStackTrace()

                    }
                },
                { error ->
                    Log.e("STDANNOUNCE2", "Error fetching the data: ${error.message}")
                }
            )
            reqQueue.add(jsonArrayRequest)
        }

        else if (userType == "other")
        {
            if (userID == null) {
                Log.e("USERSANN", "user id is null, cannot fetch announcements.")
                return
            }
            val reqQueue: RequestQueue = Volley.newRequestQueue(this)
            val rollnoint = userID.toIntOrNull() ?: 1
            Log.d("USERSANN", "User id no is : $rollnoint")
            val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Announcements/users/fetch_users_announcements.php?id=$rollnoint"
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apigetcohorts,
                null,
                { response ->
                    Log.d("USERSANN", "Fetched JSON Data: $response")
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
                    Log.e("STDANNOUNCE2", "Error fetching the data: ${error.message}")
                }
            )
            reqQueue.add(jsonArrayRequest)
        }

    }

    private fun fetchstudentannouncementsfromfaculty(){
        if (rollno == null) {
            Log.e("STDANNOUNCE", "Roll number is null, cannot fetch announcements.")
            return
        }
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val rollnoint = rollno?.toIntOrNull() ?: 1
        Log.d("ROLLLO", "Roll no is : $rollnoint")
        val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Faculty_Admin/Announcements/Students/fetchstudentannouncementsbyfaculty.php?id=$rollnoint"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apigetcohorts,
            null,
            { response ->
                Log.d("STDANNOUNCE", "Fetched JSON Data: $response")
                try {

                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val announcement = Announcement(
                            title = jsonObject.getString("Title"),
                            content = jsonObject.getString("Content"),
                            announcementBy = jsonObject.getString("Name"),
                            postedDate = jsonObject.getString("PostedDateTime"),
                            course = "Course: " + jsonObject.getString("NAME")

                        )
                        announcementList.add(announcement)
                        Log.d("AnnA", "title: ${announcement.title} | content: ${announcement.content} | course: ${announcement.course}")
                    }
                    announcementAdapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    e.printStackTrace()

                }
            },
            { error ->
                Log.e("STDANNOUNCE", "Error fetching the data: ${error.message}")
            }
        )
        reqQueue.add(jsonArrayRequest)
    } // Not used yet


}