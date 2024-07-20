package Faculty_Admin.Grading

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gece_sisapp20.R


//                                                                                      We added StudentGrade as the data type here
class StudentGradeAdapter(private val students: List<StudentGrade>, private val onItemClick: (StudentGrade) -> Unit) :
    RecyclerView.Adapter<StudentGradeAdapter.StudentGradeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentGradeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_student_grade_adapter, parent, false)
        return StudentGradeViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentGradeViewHolder, position: Int) {
        val student = students[position]
        holder.studentName.text = student.name
        holder.studentPercentage.text = student.percentage
        holder.itemView.setOnClickListener { onItemClick(student) } // Then we added val student as it is of that data type
    }

    override fun getItemCount(): Int = students.size

    class StudentGradeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName: TextView = itemView.findViewById(R.id.student_name)
        val studentPercentage: TextView = itemView.findViewById(R.id.student_percentage)
    }
}