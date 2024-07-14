package com.example.gece_sisapp20

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class CustomDatePicker(context: Context, attrs: AttributeSet) : DatePicker(context, attrs) {

    private val highlightedDates = mutableSetOf<Calendar>()

//    fun setHighlightedDates(dates: List<String>) {
//        highlightedDates.clear()
//        for (date in dates) {
//            val parts = date.split("-")
//            val day = parts[0].toInt()
//            val month = parts[1].toInt() - 1 // Month is 0-based in Calendar
//            val year = parts[2].toInt()
//
//            val calendar = Calendar.getInstance()
//            calendar.set(year, month, day)
//            highlightedDates.add(calendar)
//        }
//        invalidate()
//    }
    fun setHighlightedDates(dates: List<String>) {
        highlightedDates.clear()
        for (date in dates) {
            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            calendar.time = sdf.parse(date) ?: Date()
            highlightedDates.add(calendar)
        }
        updateHighlightedDates()
    }

//    override fun onDateChanged(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
//        super.onDateChanged(view, year, monthOfYear, dayOfMonth)
//        highlightDates()
//    }

    private fun updateHighlightedDates() {
        val year = year
        val month = month

        for (highlightCalendar in highlightedDates) {
            if (highlightCalendar.get(Calendar.YEAR) == year &&
                highlightCalendar.get(Calendar.MONTH) == month) {
                val dayOfMonth = highlightCalendar.get(Calendar.DAY_OF_MONTH)
                highlightDayOfMonth(dayOfMonth)
            }
        }
    }

    private fun highlightDayOfMonth(dayOfMonth: Int) {
        val dayPicker = getChildAt(0) as ViewGroup? ?: return
        for (i in 0 until dayPicker.childCount) {
            val view = dayPicker.getChildAt(i)
            if (view is TextView && view.text == dayOfMonth.toString()) {
                view.setTextColor(Color.RED)
                // You can customize further, e.g., set background color
            }
        }
    }

//    override fun dispatchDraw(canvas: Canvas) {
//        super.dispatchDraw(canvas)
//        highlightDates()
//    }
//
//    private fun highlightDates() {
//        val calendarView = findViewById<ViewGroup>(resources.getIdentifier("calendar_view", "id", "android"))
//        calendarView?.let {
//            for (highlightCalendar in highlightedDates) {
//                if (highlightCalendar.get(Calendar.YEAR) == this.year &&
//                    highlightCalendar.get(Calendar.MONTH) == this.month) {
//                    val day = highlightCalendar.get(Calendar.DAY_OF_MONTH).toString()
//                    findDayView(calendarView, day)?.setTextColor(Color.RED)
//                }
//            }
//        }
//    }
//
//    private fun findDayView(viewGroup: ViewGroup, day: String): TextView? {
//        for (i in 0 until viewGroup.childCount) {
//            val view = viewGroup.getChildAt(i)
//            if (view is TextView && view.text == day) {
//                return view
//            } else if (view is ViewGroup) {
//                val result = findDayView(view, day)
//                if (result != null) return result
//            }
//        }
//        return null
//    }

}
