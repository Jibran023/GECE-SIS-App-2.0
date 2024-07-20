package Student.Attendance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gece_sisapp20.R

data class AttendanceRecord(val date: String, val status: String)

class AttendanceAdapter(private val attendanceList: List<AttendanceRecord>) :
    RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_attendance, parent, false)
        return AttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val attendance = attendanceList[position]
        holder.dateText.text = attendance.date
        holder.statusText.text = attendance.status
    }

    override fun getItemCount(): Int = attendanceList.size

    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.date_text)
        val statusText: TextView = itemView.findViewById(R.id.status_text)
    }
}