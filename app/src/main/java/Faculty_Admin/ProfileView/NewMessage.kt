package Faculty_Admin.ProfileView

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.gece_sisapp20.R


class NewMessage : AppCompatActivity() {
    private var userType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_message)
        userType = intent.getStringExtra("USER_TYPE")

        val backbtn = findViewById<ImageButton>(R.id.back_button)
        backbtn.setOnClickListener {
            val intent = Intent(this, Messages::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Messages::class.java).apply {
            putExtra("USER_TYPE", userType)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}