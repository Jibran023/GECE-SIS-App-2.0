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

        val valid_credentials = mapOf(
            "js08312" to "mazerunner",
            "smh00001" to "gecesisapp"
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

            // Checking the credentials
            if (valid_credentials.containsKey(username) && valid_credentials[username] == password)
            {
                val intent = Intent(this, StudentDashboard::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            else
            {
                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
            }


        }
    }
}