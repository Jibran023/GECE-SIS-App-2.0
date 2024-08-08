package Student_Admin_Faculty.ViewCourses

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gece_sisapp20.R


//data class CardItem(val text: String)

class CoursesScreenFirstAdmin : AppCompatActivity() {
    private var userType: String? = null
    private lateinit var userID: String
    private var coursesList: ArrayList<String>? = null
    private var whotosearch: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_courses_screen_first)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()
        coursesList = intent.getStringArrayListExtra("COURSES_LIST")
        whotosearch = intent.getStringExtra("WHO_TO_SEARCH")

        Log.d("Courses Received", "Courses list received: $coursesList")

        if (coursesList != null) {
            Log.d("CoursesList", "Courses List: $coursesList")
        } else {
            Log.d("CoursesList", "Courses List is null or empty.")
        }

        var backbtn = findViewById<ImageView>(R.id.Courses_backbtn)
        backbtn.setOnClickListener{
            var intent = Intent(this, Courses_comboboxesAdmin::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                putExtra("WHO_TO_SEARCH", whotosearch)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)



        // Manually create the cardItems list
        val cardItems = mutableListOf<CardItem>()
        if (coursesList != null && coursesList!!.isNotEmpty()) {
            for (course in coursesList!!) {
                cardItems.add(CardItem(course))
            }
        } else {
            cardItems.add(CardItem("No Courses Available"))
        }

        val adapter = CardAdapter(this, cardItems)
        recyclerView.adapter = adapter
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, courses_comboboxes::class.java).apply {
            putExtra("USER_TYPE", userType)
            putExtra("USER_ID", userID)
            putExtra("WHO_TO_SEARCH", whotosearch)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}