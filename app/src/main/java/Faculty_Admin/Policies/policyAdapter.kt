package Faculty_Admin.Policies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.gece_sisapp20.R

// Define the Policy data class with the policyTitle property
data class Policy(
    val policyTitle: String
)

// Define the PolicyRecord to wrap Policy (if you need this wrapper, else use Policy directly)
data class PolicyRecord(val policy: Policy){}

class PolicyAdapter(private val context: Context, private val policyList: List<PolicyRecord>) : BaseAdapter() {

    override fun getCount(): Int {
        return policyList.size
    }

    override fun getItem(position: Int): Any {
        return policyList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.activity_policy_adapter, parent, false)

        val policyTextView = view.findViewById<TextView>(R.id.policyTextView)
        val policyRecord = policyList[position]

        policyTextView.text = policyRecord.policy.policyTitle

        return view
    }
}
