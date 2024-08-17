package Faculty_Admin.Attendance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.gece_sisapp20.R

class CheckBoxAttendanceAdapter(
    private val context: Context,
    private val items: List<String>,
    private val selectedItems: MutableMap<String, String>
) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_attendance_status, parent, false)

        val studentNameTextView = view.findViewById<TextView>(R.id.student_name)
        val checkboxPresent = view.findViewById<CheckBox>(R.id.checkbox_present)
        val checkboxAbsent = view.findViewById<CheckBox>(R.id.checkbox_absent)
        val checkboxLate = view.findViewById<CheckBox>(R.id.checkbox_late)

        val item = items[position]

        // Set student name
        studentNameTextView.text = item

        checkboxPresent.setOnCheckedChangeListener(null)
        checkboxAbsent.setOnCheckedChangeListener(null)
        checkboxLate.setOnCheckedChangeListener(null)

        // Initialize checkbox states based on selectedItems map
        when (selectedItems[item]) {
            "Present" -> {
                checkboxPresent.isChecked = true
                checkboxAbsent.isChecked = false
                checkboxLate.isChecked = false
            }
            "Absent" -> {
                checkboxPresent.isChecked = false
                checkboxAbsent.isChecked = true
                checkboxLate.isChecked = false
            }
            "Late" -> {
                checkboxPresent.isChecked = false
                checkboxAbsent.isChecked = false
                checkboxLate.isChecked = true
            }
            else -> {
                checkboxPresent.isChecked = false
                checkboxAbsent.isChecked = false
                checkboxLate.isChecked = false
            }
        }

        // Handle checkbox selection with listeners re-enabled
        checkboxPresent.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedItems[item] = "Present"
                checkboxAbsent.isChecked = false
                checkboxLate.isChecked = false
            } else if (selectedItems[item] == "Present") {
                selectedItems.remove(item)
            }
        }

        checkboxAbsent.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedItems[item] = "Absent"
                checkboxPresent.isChecked = false
                checkboxLate.isChecked = false
            } else if (selectedItems[item] == "Absent") {
                selectedItems.remove(item)
            }
        }

        checkboxLate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedItems[item] = "Late"
                checkboxPresent.isChecked = false
                checkboxAbsent.isChecked = false
            } else if (selectedItems[item] == "Late") {
                selectedItems.remove(item)
            }
        }

        return view
    }
}
