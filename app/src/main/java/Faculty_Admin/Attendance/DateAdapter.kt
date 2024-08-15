package Faculty_Admin.Attendance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gece_sisapp20.R

class DateAdapter(
    private val context: Context,
    private val dates: List<String>,
    private val onDateClick: (String) -> Unit
) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        init {
            itemView.setOnClickListener {
                val date = dates[adapterPosition]
                onDateClick(date)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.date_card_view, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.dateTextView.text = dates[position]
    }

    override fun getItemCount(): Int = dates.size
}
