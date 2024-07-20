package Faculty_Admin.Admin_Mapping

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gece_sisapp20.R
import com.example.gece_sisapp20.StudentAdapter2


class AdminMapping3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_mapping3)

        // Find views by ID
        val studentCountTextView : TextView = findViewById(R.id.studentCountTextView)
        val recyclerViewStudents : RecyclerView = findViewById(R.id.recyclerViewStudents)

        // Back button functionality
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            var intent = Intent(this, AdminMapping2::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // Sample data for students
        val students = listOf("John Doe", "Jane Smith", "Emily Johnson") // Replace with actual student data
        studentCountTextView.text = "Number of students mapped: ${students.size}"

        // Set up the RecyclerView
        recyclerViewStudents.layoutManager = LinearLayoutManager(this)
        recyclerViewStudents.adapter = StudentAdapter2(students)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, AdminMapping2::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}