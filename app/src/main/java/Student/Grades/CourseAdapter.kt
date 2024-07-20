package Student.Grades

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gece_sisapp20.R

class CourseAdapter(private val context: Context, private val courses: List<Course>) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_course_adapter, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.courseName.text = course.courseName
        holder.courseId.text = course.courseId
        holder.internalMarks.text = course.internalMarks
        holder.externalMarks.text = course.externalMarks
        holder.courseGrade.text = course.grade
        holder.courseGpa.text = course.gpa
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseName: TextView = itemView.findViewById(R.id.course_name)
        val courseId: TextView = itemView.findViewById(R.id.course_id)
        val internalMarks: TextView = itemView.findViewById(R.id.internal_marks)
        val externalMarks: TextView = itemView.findViewById(R.id.external_marks)
        val courseGrade: TextView = itemView.findViewById(R.id.course_grade)
        val courseGpa: TextView = itemView.findViewById(R.id.course_gpa)
    }
}
