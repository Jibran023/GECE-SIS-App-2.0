package com.example.gece_sisapp20

import Faculty_Admin.Admin_Mapping.AdminMapping3
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class SectionData(
    val sectionName: String,
    val sectionID: String,
    val courseName: String,
    val professorName: String,
    val userType: String?,
    val userID: String,
    val selectedCohort: String,
    val selectedCourseID: String,
    val selectedCourseFacultyID: String,
    val selectedCourseSessionID: String,
    val selectedCourseDescription: String
)

class SectionAdapter(private val sections: ArrayList<SectionData>) : RecyclerView.Adapter<SectionAdapter.SectionViewHolder>() {

    class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sectionNameTextView: TextView = itemView.findViewById(R.id.sectionNameTextView)
        val courseNameTextView: TextView = itemView.findViewById(R.id.sectionDetailsTextView)
        val professorNameTextView: TextView = itemView.findViewById(R.id.professor_offering)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_section_adapter, parent, false)
        return SectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val sectionData = sections[position]
        holder.sectionNameTextView.text = sectionData.sectionName
        holder.courseNameTextView.text = sectionData.courseName
        holder.professorNameTextView.text = sectionData.professorName

        // Proceed to the next screen when a section is clicked
        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, AdminMapping3::class.java).apply {
                putExtra("USER_TYPE", sectionData.userType)
                putExtra("SECTION_ID", sectionData.sectionID)
                putExtra("USER_ID", sectionData.userID)
                putExtra("COHORT", sectionData.selectedCohort)
                putExtra("COURSE_ID", sectionData.selectedCourseID)
                putExtra("COURSE_NAME", sectionData.courseName)
                putExtra("FACULTY_ID", sectionData.selectedCourseFacultyID)
                putExtra("INSTRUCTOR", sectionData.professorName)
                putExtra("COURSE_SESSION", sectionData.selectedCourseSessionID)
                putExtra("COURSE_DESC", sectionData.selectedCourseDescription)
                putExtra("SECTION_NAME", sectionData.sectionName) // Add section name as well
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return sections.size
    }
}
