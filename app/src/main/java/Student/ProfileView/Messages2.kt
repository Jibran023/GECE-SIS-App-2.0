package Student.ProfileView

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.R

// Data class
data class Message(
    val msgID: String,
    val userID: String,
    val title: String,
    val content: String,
    val postedDateTime: String,
    val sentTo: String,
    val receiverID: String,
    val readTime: String,
    var unreadCount: Int = 0
)

class Messages2 : AppCompatActivity() {
    private lateinit var userType: String
    private lateinit var userID: String

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private val messageList = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_messages2)
        userType = intent.getStringExtra("USER_TYPE").toString()
        userID = intent.getStringExtra("USER_ID").toString()

        val backbtn = findViewById<ImageButton>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, ProfilePicture::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with correct type
        messageAdapter = MessageAdapter(messageList, { message ->
            val intent = Intent(this, MessageDetailActivity::class.java).apply {
                Log.d("passingby", "We are in the intent")
                putExtra("USER_ID_TOCHECK", message.userID)
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                putExtra("TITLE", message.title) // Include title if needed in detail view
                putExtra("CONTENT", message.content) // Include content if needed in detail view
            }
            startActivity(intent)
        }, isDetailView = false, UserType = userType) // Set to false for the summary view
        recyclerView.adapter = messageAdapter

        fetchMessages()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, ProfilePicture::class.java).apply {
            putExtra("USER_TYPE", userType)
            putExtra("USER_ID", userID)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun fetchMessages() {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
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
                        Log.d("UnreadCount", "Final Unread Count for User ID: $userID - ${message.unreadCount}")
                    }

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
