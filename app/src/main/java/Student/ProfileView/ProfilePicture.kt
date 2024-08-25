package Student.ProfileView

import Faculty_Admin.Dashboards.AdminDashboard
import Faculty_Admin.Dashboards.FacultyDashboard
import Faculty_Admin.ProfileView.Messages
import Student.Dashboard.StudentDashboard
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
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
    private val messageList = mutableListOf<Message>()
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var unreadcount: TextView
    private var UnreadCount = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_picture)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString() // Takes studentID from the previous screen

        val dashboard_icon = findViewById<RelativeLayout>(R.id.dashboard_icon)
        val logout_icon = findViewById<RelativeLayout>(R.id.logout_id)

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

        val inbox_icon = findViewById<RelativeLayout>(R.id.inbox_icon)
        inbox_icon.setOnClickListener {
            val intent = Intent(this, Messages2::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        logout_icon.setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        unreadcount = findViewById(R.id.unread_counT)
        fetchMessages { unread ->
            Log.d("UnRead", "We have reached here")
            if (UnreadCount > 0)
            {
                unreadcount.visibility = View.VISIBLE
                unreadcount.text = "$UnreadCount"
            }
            else {
                unreadcount.visibility = View.GONE
            }

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
//            val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Student/ProfileView/studentsprofileinfo.php?id=$studentIDint"
            val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/studentsprofileinfoN.php?id=$studentIDint"

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
            val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/otherusersinfo.php?id=$studentIDint" // Need to add the api here

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

    private fun fetchMessages(callback: (String) -> Unit) {

        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
//        val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Messages/fetch_student_messages.php?FacultyName=$userID"
        val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/fetch_student_messagesN.php?FacultyName=$userID"
        Log.d("apigetcohorts", "URL: $apigetcohorts")
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apigetcohorts,
            null,
            { response ->
                try {
                    Log.d("mesae", "response: $response")
                    val uniqueUserMessages = mutableMapOf<String, Message>()
                    val unreadMessageCountMap = mutableMapOf<String, Int>()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val message = Message(
                            msgID = jsonObject.getString("MsgID"),
                            userID = jsonObject.getString("userID"),
                            title = jsonObject.getString("Title"),
                            content = jsonObject.getString("Content"),
                            postedDateTime = jsonObject.getString("PostedDateTime"),
                            sentTo = jsonObject.getString("SentTo"),
                            receiverID = jsonObject.getString("ReceiverID"),
                            readTime = jsonObject.getString("ReadTime")
                        )
                        // Calculate unread count
                        if (message.readTime == "" || message.readTime == "null") {
                            val unreadCount = unreadMessageCountMap.getOrDefault(message.userID, 0) + 1
                            unreadMessageCountMap[message.userID] = unreadCount
                            Log.d("UnreadCount", "User ID: ${message.userID} - Unread Count: $unreadCount")
                        }
                        // Store only one message per userID
                        if (!uniqueUserMessages.containsKey(message.userID)) {
                            uniqueUserMessages[message.userID] = message
                        }
                    }
                    // Assign unread counts to the corresponding messages
                    for ((userID, message) in uniqueUserMessages) {
                        message.unreadCount = unreadMessageCountMap.getOrDefault(userID, 0)
                        UnreadCount = message.unreadCount
                        Log.d("UnreadCount", "Final Unread Count for User ID: $userID - ${message.unreadCount}")
                    }
                    callback(UnreadCount.toString())

                    messageList.clear()
                    messageList.addAll(uniqueUserMessages.values)
                    messageAdapter.notifyDataSetChanged()
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