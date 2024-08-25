package Student.Dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import Student_Admin_Faculty.Complaint_Feedback.ComplaintScreenFirst
import Student.Grades.GradesScreenFirst
import Student.ProfileView.ProfilePicture
import com.example.gece_sisapp20.R
import Student.Policies.StudentDashboardPolicies
import Student.Announcement.Student_Announcement
import Student_Admin_Faculty.ViewCourses.courses_comboboxes
import Student.Attendance.student_attendance_comboboxes
import Student.ProfileView.Message
import Student.ProfileView.MessageAdapter
import android.util.Log
import android.view.View
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen



class StudentDashboard : AppCompatActivity() {
    private val messageList = mutableListOf<Message>()
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var unreadcount: TextView
    private var UnreadCount = 0
    private lateinit var userID: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_dashboard)
        val userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()

        Log.d("ID-TYPE", "ID Type: $userType and ID: $userID")

        // DONE
        val attendanceicon = findViewById<LinearLayout>(R.id.attendance_icon)
        attendanceicon.setOnClickListener {
            val intent = Intent(this, student_attendance_comboboxes::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

//        // added user and color check (DONE)
//        val policiesicon = findViewById<LinearLayout>(R.id.policies_icon)
//        policiesicon.setOnClickListener {
//            if (userType == "student")
//            {
//                val intent = Intent(this, StudentDashboardPolicies::class.java).apply {
//                    putExtra("USER_TYPE", userType)
//                    putExtra("USER_ID", userID)
//                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                }
//                startActivity(intent)
//            }
//            else if (userType == "other")
//            {
//                val intent = Intent(this, StudentDashboardPolicies::class.java).apply {
//                    putExtra("USER_TYPE", userType)
//                    putExtra("USER_ID", userID)
//                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                }
//                startActivity(intent)
//            }
//
//        }

        // user check and color check
        val complain_and_feedback = findViewById<LinearLayout>(R.id.complaintfeedbackicon)
        complain_and_feedback.setOnClickListener {
            var intent = Intent(this, ComplaintScreenFirst::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // DONE
        val announcementicon = findViewById<LinearLayout>(R.id.announcementicon)
        announcementicon.setOnClickListener {
            val intent = Intent(this, Student_Announcement::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // added user check (DONE)
        val gradesicon = findViewById<LinearLayout>(R.id.gradesicon)
        gradesicon.setOnClickListener {
            val intent = Intent(this, GradesScreenFirst::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // DONE
        val coursesicon = findViewById<LinearLayout>(R.id.coursesicon)
        coursesicon.setOnClickListener {
            var intent = Intent(this, courses_comboboxes::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // DONE - NEED TO ADD OTHERUSERSINFO API
        val profile_pic = findViewById<ImageView>(R.id.studentdashboardprofilepicture)
        profile_pic.setOnClickListener {
            var intent = Intent(this, ProfilePicture::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        unreadcount = findViewById(R.id.unread_couNT)
        fetchMessages { unread ->
            Log.d("UnRead", "We have reached here")
            if (UnreadCount > 0)
            {
                unreadcount.visibility = View.VISIBLE
                unreadcount.text = "!"
            }
            else {
                unreadcount.visibility = View.GONE
            }

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