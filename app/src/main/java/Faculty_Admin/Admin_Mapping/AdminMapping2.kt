package Faculty_Admin.Admin_Mapping

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageButton
import com.example.gece_sisapp20.R
import com.example.gece_sisapp20.SectionAdapter


class AdminMapping2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_mapping2)

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            var intent = Intent(this, AdminMapping::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        val recyclerViewSections : RecyclerView = findViewById(R.id.recyclerViewSections) // Find RecyclerView by ID

        val sections = listOf("Section 1", "Section 2", "Section 3") // Replace with actual section data

        recyclerViewSections.layoutManager = LinearLayoutManager(this)
        recyclerViewSections.adapter = SectionAdapter(sections)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, AdminMapping::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}