package Faculty_Admin.ProfileView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gece_sisapp20.R

data class Message(val sender: String, val preview: String)

class MessageAdapter(private val messages: List<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val senderTextView: TextView = view.findViewById(R.id.message_sender)
        val previewTextView: TextView = view.findViewById(R.id.message_preview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.senderTextView.text = message.sender
        holder.previewTextView.text = message.preview
    }

    override fun getItemCount() = messages.size
}