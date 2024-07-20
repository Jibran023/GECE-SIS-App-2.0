package Student_Admin_Faculty.Complaint_Feedback

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.gece_sisapp20.R


class ComplaintTrack : AppCompatActivity() {
    private var userType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_complaint_track)
        userType = intent.getStringExtra("USER_TYPE")

        val track_backbtn = findViewById<ImageButton>(R.id.back_button)
        track_backbtn.setOnClickListener {
            val intent = Intent(this, ComplaintScreenFirst::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, ComplaintScreenFirst::class.java).apply {
            putExtra("USER_TYPE", userType)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}