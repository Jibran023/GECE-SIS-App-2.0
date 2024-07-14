package com.example.gece_sisapp20

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Spinner
import android.widget.ArrayAdapter


class AdminMapping2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_mapping2)

        val recyclerViewSections : RecyclerView = findViewById(R.id.recyclerViewSections) // Find RecyclerView by ID

        val sections = listOf("Section 1", "Section 2", "Section 3") // Replace with actual section data

        recyclerViewSections.layoutManager = LinearLayoutManager(this)
//        recyclerViewSections.adapter = SectionAdapter(sections)

    }
}