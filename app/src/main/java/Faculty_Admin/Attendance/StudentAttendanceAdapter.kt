package Faculty_Admin.Attendance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gece_sisapp20.R

class StudentAttendanceAdapter(
    private val studentNames: ArrayList<String>,
    private val attendanceStatuses: ArrayList<String>
) : RecyclerView.Adapter<StudentAttendanceAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentNameTextView: TextView = itemView.findViewById(R.id.studentNameTextView)
        val attendanceStatusTextView: TextView = itemView.findViewById(R.id.attendanceStatusTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student_attendance, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.studentNameTextView.text = studentNames[position]
        holder.attendanceStatusTextView.text = attendanceStatuses[position]
    }

    override fun getItemCount(): Int {
        return studentNames.size
    }
}
