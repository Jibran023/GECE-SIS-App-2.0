package Student.ProfileView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.R

class MessageAdapter(
    private val messageList: List<Message>,
    private val onItemClicked: (Message) -> Unit,
    private val isDetailView: Boolean, // Flag to determine the view type
    private val UserType: String // UserType parameter
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView? = itemView.findViewById(R.id.messageTitle)
        private val titleTextViewDetail: TextView? = itemView.findViewById(R.id.messageTitleDetail)
        private val userIDTextView: TextView? = itemView.findViewById(R.id.messageUserID)
        private val contentTextView: TextView? = itemView.findViewById(R.id.messageContentDetail)
        private val posted_date: TextView? = itemView.findViewById(R.id.sentdate)
        private val seen_date: TextView? = itemView.findViewById(R.id.seendate)
        private val unreadCountTextView: TextView? = itemView.findViewById(R.id.unread_count)

        fun bind(message: Message) {
            Log.d("MSGADP", "We are in bind")
            if (isDetailView) {
                if (UserType == "admin"){
                    Log.d("MSGADP", "Printing admin messages")
                    titleTextViewDetail?.text = message.title
                    contentTextView?.text = message.content
                    posted_date?.text = "Sent at: ${message.postedDateTime}"
                    seen_date?.text = "Seen at: ${message.readTime}"
                    titleTextView?.visibility = View.GONE
                    userIDTextView?.visibility = View.GONE
                }
                else {
                    titleTextViewDetail?.text = message.title
                    contentTextView?.text = message.content
                    posted_date?.text = "Posted: ${message.postedDateTime}"
                    titleTextView?.visibility = View.GONE
                    userIDTextView?.visibility = View.GONE
                }
            } else {
                if (UserType != "admin") {
//                    titleTextView?.text = message.title
                    fetchUserName(message.userID) { UserName, dept ->
                        titleTextView?.text = "$UserName | Dept: $dept"
                    }
                    unreadCountTextView?.visibility = View.VISIBLE
                    unreadCountTextView?.text = "${message.unreadCount}"

                    titleTextViewDetail?.visibility = View.GONE
                    contentTextView?.visibility = View.GONE
                } else {
//                    titleTextView?.text = message.title
                    fetchReceiverName(message.receiverID) { receiverName, cohort, rollno ->
                        titleTextView?.text = "$receiverName | $cohort"
                        userIDTextView?.text = "Rollnumber: $rollno"
                    }
                    titleTextViewDetail?.visibility = View.GONE
                    contentTextView?.visibility = View.GONE
                }
            }

//            // Handle unread messages count
//            if (UserType == "student"){
//                if (message.readTime == "" || message.readTime == "null") {
//                    Log.d("MSGADP", "Message readtime was null")
//                    unreadCountTextView?.visibility = View.VISIBLE
//                    unreadCountTextView?.text = "${message.unreadCount}"
//                } else {
//                    Log.d("MSGADP", "Message readtime was not null")
//                    unreadCountTextView?.visibility = View.GONE
//                }
//            }
//            else {
//                if (message.readTime == "" || message.readTime == "null") {
//                    Log.d("MSGADP", "Message readtime was null")
//                    unreadCountTextView?.visibility = View.VISIBLE
//                    unreadCountTextView?.text = "!"
//                } else {
//                    Log.d("MSGADP", "Message readtime was not null")
//                    unreadCountTextView?.visibility = View.GONE
//                }
//            }

            itemView.setOnClickListener { onItemClicked(message) }
        }

        private fun fetchReceiverName(receiverID: String, callback: (String, String, String) -> Unit) {
            val reqQueue: RequestQueue = Volley.newRequestQueue(itemView.context)
            val apiUrl = "${LoginScreen.BASE_URL}/geceapi/Messages/fetchReceiverName.php?FacultyName=$receiverID"

            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                { response ->
                    Log.d("MessageAdapter", "Fetched JSON Data: $response")
                    try {
                        if (response.length() > 0) {
                            val jsonObject = response.getJSONObject(0)
                            val name = jsonObject.getString("Name") // Assuming 'Name' is the key for the name
                            val cohort = jsonObject.getString("cohort")
                            val rollno = jsonObject.getString("RollNumber")
                            callback(name, cohort, rollno)
                        } else {
                            callback("Unknown Receiver", "Unknown Dept", "Unknown Dept")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        callback("Error fetching name", "Error fetching dept", "Error fetching dept")
                    }
                },
                { error ->
                    Log.e("MessageAdapter", "Error fetching the data: ${error.message}")
                    callback("Error fetching name", "Error fetching dept", "Error fetching dept")
                }
            )

            reqQueue.add(jsonArrayRequest)
        }

        private fun fetchUserName(userID: String, callback: (String, String) -> Unit) {
            val reqQueue: RequestQueue = Volley.newRequestQueue(itemView.context)
            val apiUrl = "${LoginScreen.BASE_URL}/geceapi/Messages/fetchUserName.php?FacultyName=$userID"

            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                { response ->
                    Log.d("MessageAdapter", "Fetched JSON Data: $response")
                    try {
                        if (response.length() > 0) {
                            val jsonObject = response.getJSONObject(0)
                            val name = jsonObject.getString("Name") // Assuming 'Name' is the key for the name
                            val dept = jsonObject.getString("department")
                            callback(name, dept)
                        } else {
                            callback("Unknown Receiver", "Unknown Dept")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        callback("Error fetching name", "Error fetching dept")
                    }
                },
                { error ->
                    Log.e("MessageAdapter", "Error fetching the data: ${error.message}")
                    callback("Error fetching name", "Error fetching dept")
                }
            )

            reqQueue.add(jsonArrayRequest)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutId = if (isDetailView) R.layout.item_message_detail else R.layout.item_messages
        val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messageList[position])

    }

    override fun getItemCount(): Int = messageList.size
}
