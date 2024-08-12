package Faculty_Admin.Announcement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckedTextView
import android.widget.TextView

class CheckboxAdapter(
    private val items: List<String>,
    private val selectedItems: MutableList<String>
) : BaseAdapter() {

    private val allOption = "All"
    private var isAllSelected: Boolean = false

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_multiple_choice, parent, false)


        val checkedTextView = view.findViewById<CheckedTextView>(android.R.id.text1)
        val item = items[position]
        checkedTextView.text = item

        // Set the checked state based on the selectedItems list
        checkedTextView.isChecked = selectedItems.contains(item) || (item == allOption && isAllSelected)

        // Update the selection when clicked
        checkedTextView.setOnClickListener {
            if (item == allOption) {
                handleAllOption()
            } else {
                if (checkedTextView.isChecked) {
                    selectedItems.remove(item)
                } else {
                    selectedItems.add(item)
                }
                // Toggle the checked state
                checkedTextView.isChecked = !checkedTextView.isChecked
                // Update the "All" option state
                updateAllOptionState()
            }
        }

        return view
    }

    private fun handleAllOption() {
        if (isAllSelected) {
            // If "All" is currently selected, deselect all
            selectedItems.clear()
            isAllSelected = false
        } else {
            // If "All" is not selected, select all
            selectedItems.clear()
            selectedItems.add(allOption)
            selectedItems.addAll(items.filter { it != allOption })
            isAllSelected = true
        }
        notifyDataSetChanged()
    }

    private fun updateAllOptionState() {
        isAllSelected = items.all { selectedItems.contains(it) } || selectedItems.contains(allOption)
    }
}
