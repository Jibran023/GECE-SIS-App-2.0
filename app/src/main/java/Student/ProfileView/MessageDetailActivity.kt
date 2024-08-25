package Student.ProfileView

import Faculty_Admin.ProfileView.Messages
import Faculty_Admin.ProfileView.Profile_Faculty
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.R
import org.json.JSONObject

class MessageDetailActivity : AppCompatActivity() {
    private lateinit var userID: String
    private lateinit var userType: String

    private lateinit var userIDtocheck: String
    private lateinit var receiverid: String
    private val messageList = mutableListOf<Message>()
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_detail)

        userType = intent.getStringExtra("USER_TYPE").toString()
        userID = intent.getStringExtra("USER_ID").toString()
        userIDtocheck = intent.getStringExtra("USER_ID_TOCHECK").toString()
        receiverid = intent.getStringExtra("RECEIVERID").toString()

        val backbtn = findViewById<ImageButton>(R.id.backbtn)
        backbtn.setOnClickListener {
            if (userType == "student") {
                Log.d("GoingWhere", "Going to Messages 2 as a student")
                val intent = Intent(this, Messages2::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            else {
                Log.d("GoingWhere", "Going to Messages as a admin")
                val intent = Intent(this, Messages::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        }

        if (userType == "admin"){
            if (receiverid != null && userIDtocheck != null){
                Log.d("apigetcohorts", "receiver id and useridtocheck are not null")
                fetchUserSentmsgs()
            }
            else{
                Log.d("apigetcohorts", "receiver id or useridtocheck is null")
            }
        }
        else{
            fetchAllMessagesForUser()
        }


        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter(messageList, { message ->
            // Handle item click if needed
        }, isDetailView = true, userType)
        recyclerView.adapter = messageAdapter




    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (userType == "student") {
            Log.d("GoingWhere", "Going to Messages 2 as a student")
            val intent = Intent(this, Messages2::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
        else {
            Log.d("GoingWhere", "Going to Messages as a admin")
            val intent = Intent(this, Messages::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

    }


    private fun fetchAllMessagesForUser() {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
//        val url = "${LoginScreen.BASE_URL}/geceapi/Messages/fetch_student_messages_all_of_user.php?FacultyName=$userID&USER=$userIDtocheck"
        val url = "${LoginScreen.BASE_URL}/geceapi/fetch_student_messages_all_of_userN.php?FacultyName=$userID&USER=$userIDtocheck"
        Log.d("ExtResponse", "URL: $url")
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                    Log.d("ExtResponse","Response: $response")
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
                        // Only add the message to the list if the content is not empty
                        if (message.content.isNotEmpty()) {
                            messageList.add(message)
                        }
                    }
                    messageAdapter.notifyDataSetChanged()
                    // Update read times after fetching all messages
                    updateReadTimes()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                Log.e("MessageDetailActivity", "Error fetching the data: ${error.message}")
            }
        )
        reqQueue.add(jsonArrayRequest)
    }

    private fun fetchUserSentmsgs() {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
//        val url = "${LoginScreen.BASE_URL}/geceapi/Messages/fetch_student_messages_all_of_user.php?FacultyName=$receiverid&USER=$userIDtocheck"
        val url = "${LoginScreen.BASE_URL}/geceapi/fetch_student_messages_all_of_userN.php?FacultyName=$receiverid&USER=$userIDtocheck"
        Log.d("ExtResponse", "URL: $url")
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                    Log.d("ExtResponse","Response: $response")
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
                        // Only add the message to the list if the content is not empty
                        if (message.content.isNotEmpty()) {
                            messageList.add(message)
                        }
                    }
                    messageAdapter.notifyDataSetChanged()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                Log.e("MessageDetailActivity", "Error fetching the data: ${error.message}")
            }
        )
        reqQueue.add(jsonArrayRequest)
    }


    private fun updateReadTimes() {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
//        val baseUrl = "${LoginScreen.BASE_URL}/geceapi/Messages/update_readtime.php"
        val baseUrl = "${LoginScreen.BASE_URL}/geceapi/update_readtimeN.php"
        // Collect message IDs and prepare URL
        val messageIds = messageList.filter { it.readTime == "null" || it.readTime == "" }.joinToString(separator = ",") { it.msgID }
        Log.d("updateread", "URL: $baseUrl")
        Log.d("updateread", "URL: $messageIds")
        if (messageIds.isNotEmpty()) {
            val url = "$baseUrl?MsgIDs=$messageIds&ReadTime=${getCurrentDateTime()}"
            Log.d("updateread", "URL: $url")
            val stringRequest = StringRequest(
                Request.Method.GET,
                url,
                { response ->
                    Log.d("UpdateReadTime", "Success: $response")
                },
                { error ->
                    Log.e("UpdateReadTime", "Error: ${error.message}")
                }
            )

            reqQueue.add(stringRequest)
        }
    }

    // Function to get current date and time in the desired format
    private fun getCurrentDateTime(): String {
        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
        return dateFormat.format(java.util.Date())
    }


}
