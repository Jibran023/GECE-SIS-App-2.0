package Faculty_Admin.Attendance

import Faculty_Admin.Dashboards.AdminDashboard
import Faculty_Admin.Dashboards.FacultyDashboard
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.R
import org.json.JSONObject

class Check_Warnings : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID : String
    private lateinit var set_warnings: Button
    private lateinit var send_warnings: Button

    private lateinit var edit_student_attendance: Dialog
    private lateinit var warningSelections: MutableList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_check_warnings)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()

        val back_btn = findViewById<ImageButton>(R.id.backbtn)
        back_btn.setOnClickListener {
            Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
            if (userType == "faculty") {
                val intent = Intent(this, FacultyDashboard::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            } else if (userType == "admin") {
                val intent = Intent(this, FacultyChooseAttendance::class.java).apply {
                    putExtra("USER_TYPE", userType)
                    putExtra("USER_ID", userID)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        }

        set_warnings = findViewById(R.id.set_warning_dates)
        set_warnings.setOnClickListener{
            showStudentRemoveDialog()
        }
        send_warnings = findViewById(R.id.button_view_attendance)



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
            val intent = Intent(this, FacultyChooseAttendance::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
        finish()
    }

    private fun showStudentRemoveDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_set_warnings) // You'll need to create this layout

        val warningTitles = listOf("1st Warning", "2nd Warning", "3rd Warning", "4th Warning")
        val spinnerIds = listOf(R.id.warning_spinner_1, R.id.warning_spinner_2, R.id.warning_spinner_3, R.id.warning_spinner_4)

        warningSelections = MutableList(warningTitles.size) { 1 } // Initialize with default values (1)

        // Set up the spinners for each warning
        for (i in warningTitles.indices) {
            val spinner = dialog.findViewById<Spinner>(spinnerIds[i])
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, (1..10).toList())
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            // Handle selection
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    warningSelections[i] = position + 1 // Save the selected number
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }
        }

        val applyButton = dialog.findViewById<Button>(R.id.apply_button)
        applyButton.setOnClickListener {
            // The `warningSelections` list now contains the selected numbers
            Toast.makeText(this, "Selected Warnings: $warningSelections", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        // Adjust the dialog size
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.show()
    }



}