package Faculty_Admin.ProfileView

import Student.ProfileView.Message
import Student.ProfileView.MessageAdapter
import Student.ProfileView.MessageDetailActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
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
import com.example.gece_sisapp20.R


class Messages : AppCompatActivity() {
    private lateinit var userType: String
    private lateinit var userID: String

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private val messageList = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_messages)
        userType = intent.getStringExtra("USER_TYPE").toString()
        userID = intent.getStringExtra("USER_ID").toString()

        val backbtn = findViewById<ImageButton>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, Profile_Faculty::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        val new_msg = findViewById<ImageView>(R.id.newmsg)
        new_msg.setOnClickListener {
            if (userType == "admin"){
                val intent = Intent(this, NewMessage::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "You're not authorized to send messages!", Toast.LENGTH_SHORT).show()
            }

        }

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with correct type
        messageAdapter = MessageAdapter(messageList, { message ->
            val intent = Intent(this, MessageDetailActivity::class.java).apply {
                Log.d("passingby", "We are in the intent")
                putExtra("USER_ID_TOCHECK", message.userID)
                putExtra("RECEIVERID", message.receiverID)
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                putExtra("TITLE", message.title) // Include title if needed in detail view
                putExtra("CONTENT", message.content) // Include content if needed in detail view
                Log.d("apigetcohorts", "UserID: ${message.userID}| ReceiverID: ${message.receiverID}| Title: ${message.title}| Content: ${message.content}")
            }
            startActivity(intent)
        }, isDetailView = false, UserType = userType) // Set to false for the summary view
        recyclerView.adapter = messageAdapter

        fetchSentMessages() // this will fetch the messages user sent



    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Profile_Faculty::class.java).apply {
            putExtra("USER_TYPE", userType)
            putExtra("USER_ID", userID)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun fetchSentMessages() {
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
//        val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/Messages/fetch_user_sent_messages.php?FacultyName=$userID"
        val apigetcohorts = "${LoginScreen.BASE_URL}/geceapi/fetch_user_sent_messagesN.php?FacultyName=$userID"
        Log.d("apigetcohorts", "URL: $apigetcohorts")
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apigetcohorts,
            null,
            { response ->
                try {
                    Log.d("mesae", "response: $response")
                    val uniqueUserMessages = mutableMapOf<String, Message>()
                    messageList.clear()
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
                        messageList.add(message)
                    }
//                    messageList.clear()
//                    messageList.addAll(uniqueUserMessages.values)
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

//        // Dummy messages data
//        val messages = listOf(
//            Message("Alice", "Hi there, how are you?"),
//            Message("Bob", "Don't forget the meeting tomorrow."),
//            Message("Charlie", "Happy birthday!"),
//            Message("Diana", "Can we reschedule our appointment?"),
//            Message("Eve", "Your package has been delivered."),
//            Message("Frank", "Let's catch up soon.")
//        )
//
//        // Setup RecyclerView
//        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = MessageAdapter(messages)