package Faculty_Admin.Announcement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gece_sisapp20.R

data class Announcement(
    val title: String,
    val content: String,
    val announcementBy: String,
    val postedDate: String
)


class AnnouncementAdapter(private val announcements: List<Announcement>) :
    RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_announcement, parent, false)
        return AnnouncementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val announcement = announcements[position]
        holder.titleTextView.text = announcement.title
        holder.contentTextView.text = announcement.content
        holder.announcementByTextView.text = "By: ${announcement.announcementBy}"
        holder.postedDateTextView.text = "Posted on: ${announcement.postedDate}"
    }

    override fun getItemCount(): Int {
        return announcements.size
    }

    class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val announcementByTextView: TextView = itemView.findViewById(R.id.announcementByTextView)
        val postedDateTextView: TextView = itemView.findViewById(R.id.postedDateTextView)
    }
}
