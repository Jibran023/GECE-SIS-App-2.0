package Faculty_Admin.Policies

import Faculty_Admin.Dashboards.AdminDashboard
import Faculty_Admin.Dashboards.FacultyDashboard
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.gece_sisapp20.R


class FacultyDashboardPolicies : AppCompatActivity() {
    private var userType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_dashboard_policies)
        userType = intent.getStringExtra("USER_TYPE")

        val listView = findViewById<ListView>(R.id.listView)

        // Sample data
        val policyRecords = listOf(
            PolicyRecord(Policy("Policy 1 Title")),
            PolicyRecord(Policy("Policy 2 Title")),
            PolicyRecord(Policy("Policy 3 Title"))
        )

        val adapter = PolicyAdapter(this, policyRecords)
        listView.adapter = adapter

        val policies_backbtn = findViewById<ImageView>(R.id.policiesback_button)
        policies_backbtn.setOnClickListener {
            if (userType == "faculty") {
                val intent = Intent(this, FacultyDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            } else if (userType == "admin") {
                val intent = Intent(this, AdminDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (userType == "faculty") {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FacultyDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        } else if (userType == "admin") {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AdminDashboard::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
        finish()
    }

}