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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_courses_screen_first)

        var backbtn = findViewById<ImageView>(R.id.Courses_backbtn)
        backbtn.setOnClickListener{
            var intent = Intent(this, StudentDashboard::class.java)
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
}