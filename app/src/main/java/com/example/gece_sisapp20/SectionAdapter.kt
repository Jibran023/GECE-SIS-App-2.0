package com.example.gece_sisapp20

import Faculty_Admin.Admin_Mapping.AdminMapping3
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SectionAdapter(private val sections: List<String>) : RecyclerView.Adapter<SectionAdapter.SectionViewHolder>() {

    class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sectionNameTextView: TextView = itemView.findViewById(R.id.sectionNameTextView)
        val sectionDetailsTextView: TextView = itemView.findViewById(R.id.sectionDetailsTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_section_adapter, parent, false)
        return SectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = sections[position]
        holder.sectionNameTextView.text = section // Modify this as per your data structure
        holder.sectionDetailsTextView.text = "Details about $section" // Example details

        // We added this to proceed to the next screen when a section is clicked
        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, AdminMapping3::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return sections.size
    }


}
