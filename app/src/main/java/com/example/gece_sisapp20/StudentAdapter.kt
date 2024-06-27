package com.example.gece_sisapp20

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Student(val name: String, var attendance: String)

class StudentAdapter(private val students: List<Student>) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.studentName.text = student.name

        // Handle checkboxes based on the student's current attendance status
        holder.checkPresent.setOnCheckedChangeListener(null)
        holder.checkAbsent.setOnCheckedChangeListener(null)
        holder.checkLate.setOnCheckedChangeListener(null)

        holder.checkPresent.isChecked = student.attendance == "present"
        holder.checkAbsent.isChecked = student.attendance == "absent"
        holder.checkLate.isChecked = student.attendance == "late"

        holder.checkPresent.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                student.attendance = "present"
                holder.checkAbsent.isChecked = false
                holder.checkLate.isChecked = false
            }
        }

        holder.checkAbsent.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                student.attendance = "absent"
                holder.checkPresent.isChecked = false
                holder.checkLate.isChecked = false
            }
        }

        holder.checkLate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                student.attendance = "late"
                holder.checkPresent.isChecked = false
                holder.checkAbsent.isChecked = false
            }
        }
    }

    override fun getItemCount(): Int = students.size

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName: TextView = itemView.findViewById(R.id.student_name)
        val checkPresent: CheckBox = itemView.findViewById(R.id.check_present)
        val checkAbsent: CheckBox = itemView.findViewById(R.id.check_absent)
        val checkLate: CheckBox = itemView.findViewById(R.id.check_late)
    }
}