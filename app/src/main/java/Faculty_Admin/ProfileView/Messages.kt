package Faculty_Admin.ProfileView

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gece_sisapp20.R


class Messages : AppCompatActivity() {
    private var userType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_messages)
        userType = intent.getStringExtra("USER_TYPE")

        val backbtn = findViewById<ImageButton>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, Profile_Faculty::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        val new_msg = findViewById<ImageView>(R.id.newmsg)
        new_msg.setOnClickListener {
            val intent = Intent(this, NewMessage::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }


        // Dummy messages data
        val messages = listOf(
            Message("Alice", "Hi there, how are you?"),
            Message("Bob", "Don't forget the meeting tomorrow."),
            Message("Charlie", "Happy birthday!"),
            Message("Diana", "Can we reschedule our appointment?"),
            Message("Eve", "Your package has been delivered."),
            Message("Frank", "Let's catch up soon.")
        )

        // Setup RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MessageAdapter(messages)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Profile_Faculty::class.java).apply {
            putExtra("USER_TYPE", userType)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}