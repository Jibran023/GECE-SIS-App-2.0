package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


data class CardItem(val text: String)

class CoursesScreenFirst : AppCompatActivity() {
    private var userType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_courses_screen_first)
        userType = intent.getStringExtra("USER_TYPE")

        var backbtn = findViewById<ImageView>(R.id.Courses_backbtn)
        backbtn.setOnClickListener{
            var intent = Intent(this, courses_comboboxes::class.java).apply {
                putExtra("USER_TYPE", userType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Sample data
        val cardItems = listOf(
            CardItem("Math"),
            CardItem("Physics"),
            CardItem("Psychology"),
            CardItem("Statistics"),
            CardItem("Child development")
        )

        val adapter = CardAdapter(this, cardItems)
        recyclerView.adapter = adapter
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, courses_comboboxes::class.java).apply {
            putExtra("USER_TYPE", userType)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}