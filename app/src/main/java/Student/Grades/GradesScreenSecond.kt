package Student.Grades

import android.content.Intent
import androidx.activity.enableEdgeToEdge
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.gece_sisapp20.LoginScreen
import com.example.gece_sisapp20.R


data class Course(val courseName: String, val courseId: String,  val internalMarks: String, val externalMarks: String,val grade: String, val gpa: String)

class GradesScreenSecond : AppCompatActivity() {
    private var userType: String? = null
    private var rollNumber: String? = null
    private lateinit var userID: String
    private var courseID: String? = null
    private lateinit var selectedSessionDescription: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_grades_screen_second)
        userType = intent.getStringExtra("USER_TYPE")
        userID = intent.getStringExtra("USER_ID").toString()
        rollNumber = intent.getStringExtra("ROLL_NUMBER") // Assuming you pass this from the first screen
        courseID = intent.getStringExtra("COURSE_ID")     // Assuming you pass this from the first screen
        selectedSessionDescription = intent.getStringExtra("SESSION").toString()

        val backbtn = findViewById<ImageView>(R.id.gradesscrnsecond_backbtn)
        backbtn.setOnClickListener{
            val intent = Intent(this, GradesScreenFirst::class.java).apply {
                putExtra("USER_TYPE", userType)
                putExtra("USER_ID", userID)
                putExtra("ROLL_NUMBER", rollNumber)  // Pass the roll number
                putExtra("COURSE_ID", courseID)  // Pass the course ID
                putExtra("SESSION", selectedSessionDescription)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch and display grades data
        fetchGradesData(recyclerView)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, GradesScreenFirst::class.java).apply {
            putExtra("USER_TYPE", userType)
            putExtra("USER_ID", userID)
            putExtra("ROLL_NUMBER", rollNumber)  // Pass the roll number
            putExtra("COURSE_ID", courseID)
            putExtra("SESSION", selectedSessionDescription)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun fetchGradesData(recyclerView: RecyclerView) {
        if (rollNumber == null || courseID == null) {
            Log.e("GradesScreenSecond", "Roll number or course ID is missing")
            return
        }
        Log.d("ROLLNUMBER", "$rollNumber")
        Log.d("COURSE_ID", "$courseID")


        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apiUrl = "${LoginScreen.BASE_URL}geceapi/Student/grades/courseGradeinfo.php?rollNumber=$rollNumber&courseID=$courseID"

//        Log.d("url12345", apiUrl)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apiUrl,
            null,
            { response ->
                Log.d("GradesScreenSecond", "Fetched JSON Data: $response")
                try {
                    val courses = mutableListOf<Course>()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
//                        val courseName = jsonObject.getString("CourseName")
                        val courseId = jsonObject.getString("CourseID")
                        val internalMarks = jsonObject.getString("Internal")
                        val externalMarks = jsonObject.getString("External")
                        val totalMarks = jsonObject.getString("TotalMarks")
                        val grade = jsonObject.getString("Grade")
                        val gpa = jsonObject.getString("GPA")

                        courses.add(Course( courseId, internalMarks, externalMarks, totalMarks, grade, gpa))
                    }

                    val adapter = CourseAdapter(this, courses)
                    recyclerView.adapter = adapter
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                Log.e("GradesScreenSecond", "Error fetching the data: ${error.message}")
            }
        )
        reqQueue.add(jsonArrayRequest)
    }
}