package Faculty_Admin.Grading

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.gece_sisapp20.R


class Faculty_markGrades4 : AppCompatActivity() {
    private var userType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_mark_grades4)
        userType = intent.getStringExtra("USER_TYPE")

        val studentNameTextView: TextView = findViewById(R.id.student_name)
        val internalMarksEditText: EditText = findViewById(R.id.internal_marks)
        val externalMarksEditText: EditText = findViewById(R.id.external_marks)
        val totalMarksEditText: EditText = findViewById(R.id.total_marks)
        val gradeEditText: EditText = findViewById(R.id.grade)
        val gpaEditText: EditText = findViewById(R.id.gpa)

        // Retrieving student data from Intent (Passed from previous screen)
        val studentName = intent.getStringExtra("student_name")
        val studentPercentage = intent.getStringExtra("student_percentage")


        // Dummy student data
        studentNameTextView.text = studentName
        internalMarksEditText.setText("85")
        externalMarksEditText.setText("90")
        totalMarksEditText.setText("175")
        gradeEditText.setText("A")
        gpaEditText.setText("4.0")



        val backbtn = findViewById<ImageView>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, Faculty_markGrades3::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }


        // Save changes
        val saveButton: Button = findViewById(R.id.savebtn)
        saveButton.setOnClickListener {
            enableEditing(false, internalMarksEditText, externalMarksEditText, totalMarksEditText, gradeEditText, gpaEditText)
            saveButton.isEnabled = false
            // Handle saving data (e.g., update the database or notify the server)
        }

        // Enable editing
        val editButton: Button = findViewById(R.id.editbtn)
        editButton.setOnClickListener {
            enableEditing(true, internalMarksEditText, externalMarksEditText, totalMarksEditText, gradeEditText, gpaEditText)
            saveButton.isEnabled = true
        }
    }

    private fun enableEditing(enabled: Boolean, vararg editTexts: EditText)
    {
        for (editText in editTexts) {
            editText.isEnabled = enabled
            editText.inputType = if (enabled) InputType.TYPE_CLASS_NUMBER else InputType.TYPE_NULL
        }
    }

    override fun onBackPressed()
    {
        super.onBackPressed()
        val intent = Intent(this, Faculty_markGrades3::class.java).apply {
            putExtra("USER_TYPE", userType)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}