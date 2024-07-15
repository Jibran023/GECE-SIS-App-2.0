package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loginscreen)

        val loginbtn = findViewById<Button>(R.id.loginbutton)
        val username_input = findViewById<EditText>(R.id.username_input)
        val password_input = findViewById<EditText>(R.id.password_input)

        val valid_credentials_students = mapOf(
            "js08312" to "mazerunner",
            "smh00001" to "gecesisapp"
        )
        val valid_credentials_faculty = mapOf(
            "mm04321" to "movania",
            "ms05432" to "msamad"
        )
        val valid_credentials_admin = mapOf(
            "muzammil" to "smh123",
            "jibran" to "js08312"
        )


        loginbtn.setOnClickListener {
            val username = username_input.text.toString().trim()
            val password = password_input.text.toString().trim()

            // Checking if username or password is empty
            if (username.isEmpty())
            {
                username_input.error = "Username cannot be empty"
                return@setOnClickListener
            }
            if (password.isEmpty())
            {
                password_input.error = "Password cannot be empty"
                return@setOnClickListener
            }

            var userType: String? = null

            // Checking the credentials for students
            if (valid_credentials_students.containsKey(username) && valid_credentials_students[username] == password) {
                userType = "student"
            }
            // Checking the credentials for faculty
            else if (valid_credentials_faculty.containsKey(username) && valid_credentials_faculty[username] == password) {
                userType = "faculty"
            }
            // Checking the credentials for admin
            else if (valid_credentials_admin.containsKey(username) && valid_credentials_admin[username] == password) {
                userType = "admin"
            }


            // Navigate to the respective dashboard based on the user type
            when (userType) {
                "student" -> {
                    Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, StudentDashboard::class.java).apply {
                        putExtra("USER_TYPE", userType)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
                "faculty" -> {
                    Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, FacultyDashboard::class.java).apply {
                        putExtra("USER_TYPE", userType)
//                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
                "admin" -> {
                    Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AdminDashboard::class.java).apply {
                        putExtra("USER_TYPE", userType)
//                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
                else -> {
                    Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
                }
            }

//            putExtra("USER_TYPE", userType)


//            // Checking the credentials for students
//            if (valid_credentials_students.containsKey(username) && valid_credentials_students[username] == password)
//            {
//                val intent = Intent(this, StudentDashboard::class.java).apply {
//                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                }
//                startActivity(intent)
//            }
//            // Checking the credentials for faculty
//            else if (valid_credentials_faculty.containsKey(username) && valid_credentials_faculty[username] == password)
//            {
//                val intent = Intent(this, FacultyDashboard::class.java).apply {
//                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                }
//                startActivity(intent)
//            }
//            // Checking the credentials for admin
//            else if (valid_credentials_admin.containsKey(username) && valid_credentials_admin[username] == password)
//            {
//                val intent = Intent(this, AdminDashboard::class.java).apply {
//                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                }
//                startActivity(intent)
//            }
//
//            else
//            {
//                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
//            }


        }
    }
}