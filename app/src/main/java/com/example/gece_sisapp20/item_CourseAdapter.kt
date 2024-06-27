package com.example.gece_sisapp20

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


// Updated data class to hold a single section
data class AttendanceCourse(val name: String, val section: String)

// Adapter class to bind data to RecyclerView
class AttendanceCourseAdapter(private val courses: List<AttendanceCourse>) :
    RecyclerView.Adapter<AttendanceCourseAdapter.AttendanceCourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceCourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_course_adapter, parent, false)
        return AttendanceCourseViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AttendanceCourseViewHolder, position: Int) {
        val course = courses[position]
        holder.courseName.text = course.name
        holder.sectionName.text = "Section: ${course.section}"
    }

    override fun getItemCount(): Int = courses.size

    class AttendanceCourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseName: TextView = itemView.findViewById(R.id.course_name)
        val sectionName: TextView = itemView.findViewById(R.id.section_name)
    }
}