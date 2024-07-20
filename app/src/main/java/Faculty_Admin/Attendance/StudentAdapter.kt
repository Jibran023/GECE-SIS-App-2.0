package Faculty_Admin.Attendance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gece_sisapp20.R

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

        holder.bind(student)
    }

    override fun getItemCount(): Int = students.size

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName: TextView = itemView.findViewById(R.id.student_name)
        val checkPresent: CheckBox = itemView.findViewById(R.id.check_present)
        val checkAbsent: CheckBox = itemView.findViewById(R.id.check_absent)
        val checkLate: CheckBox = itemView.findViewById(R.id.check_late)

        fun bind(student: Student) {
            checkPresent.setOnCheckedChangeListener(null)
            checkAbsent.setOnCheckedChangeListener(null)
            checkLate.setOnCheckedChangeListener(null)

            checkPresent.isChecked = student.attendance == "present"
            checkAbsent.isChecked = student.attendance == "absent"
            checkLate.isChecked = student.attendance == "late"

            checkPresent.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    student.attendance = "present"
                    checkAbsent.isChecked = false
                    checkLate.isChecked = false
                }
            }

            checkAbsent.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    student.attendance = "absent"
                    checkPresent.isChecked = false
                    checkLate.isChecked = false
                }
            }

            checkLate.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    student.attendance = "late"
                    checkPresent.isChecked = false
                    checkAbsent.isChecked = false
                }
            }
        }
    }
}