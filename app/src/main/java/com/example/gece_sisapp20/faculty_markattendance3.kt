package com.example.gece_sisapp20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import java.util.Calendar
import android.graphics.Color
import android.widget.TextView


class faculty_markattendance3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_markattendance3)

        val backbtn = findViewById<ImageView>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, faculty_markattendance2::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // Retrieve class dates and selected section from previous screen
        val classDates = intent.getStringArrayListExtra("class_dates") ?: listOf()
        val selectedSection = intent.getStringExtra("selected_section")

        // Dummy student data
        val students = listOf(
            Student("Alice", "present"),
            Student("Bob", "absent"),
            Student("Charlie", "late"),
            Student("Diana", "present"),
            Student("Eve", "absent"),
            Student("Frank", "late")
        )

        // Setup RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StudentAdapter(students)

        // DatePicker setup
        val datePicker: DatePicker = findViewById(R.id.date_picker)
        val calendar = Calendar.getInstance()

        datePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))
        { _, year, monthOfYear, dayOfMonth ->
            // Handle date change | You can use the selected date for attendance marking
            val selectedDate = "$dayOfMonth-${monthOfYear + 1}-$year"
            // Do something with the selected date
        }

        // Highlight class dates
        highlightClassDates(classDates, datePicker)

        // Set DatePicker constraints
        val maxDate = calendar.timeInMillis // Current date
        calendar.add(Calendar.DAY_OF_YEAR, -1) // Subtract 1 day to get the past 24 hours
        val minDate = calendar.timeInMillis

        datePicker.maxDate = maxDate
        datePicker.minDate = minDate

        // Setup RecyclerView scroll listener to show the save button when scrolled to bottom
        val saveButton: Button = findViewById(R.id.savebutton)

        recyclerView.addOnScrollListener(object : OnScrollListener() { // to detect when the user has scrolled to the bottom of the list
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                    saveButton.visibility = Button.VISIBLE
                } else {
                    saveButton.visibility = Button.GONE
                }
            }
        })

    }

    private fun highlightClassDates(classDates: List<String>, datePicker: DatePicker) {
        // Parse the dates and highlight them on the calendar
        for (date in classDates) {
            val parts = date.split("-")
            val day = parts[0].toInt()
            val month = parts[1].toInt() - 1 // Month is 0-based in Calendar
            val year = parts[2].toInt()

            val highlightCalendar = Calendar.getInstance()
            highlightCalendar.set(year, month, day)

            if (highlightCalendar[Calendar.YEAR] == datePicker.year &&
                highlightCalendar[Calendar.MONTH] == datePicker.month) {
                val dayView = datePicker.findViewWithTag(day.toString()) as? TextView
                dayView?.setBackgroundColor(Color.YELLOW)
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, faculty_markattendance2::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }



}