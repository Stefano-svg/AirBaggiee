package com.example.airbaggie

import Travel
import TravelRepository
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import org.koin.android.ext.android.inject
import java.util.Calendar

class ChooseActivity : AppCompatActivity() {

    lateinit var editTextDate: EditText
    lateinit var editTextDate2: EditText
    lateinit var calendar1: Calendar
    lateinit var calendar2: Calendar
    lateinit var destination: String
    lateinit var startdate: String
    var enddate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_choose)
        val travelRepository: TravelRepository by inject()

        var travel = travelRepository.travel
        var cont = travelRepository.cont
        Log.d("cont:", travelRepository.cont.toString())
        val citySuggestions = arrayOf(
            "New York", "Los Angeles", "Chicago", "San Francisco", "Boston", "Miami",
            "Washington D.C.", "Seattle", "Las Vegas", "Denver", "Houston", "Dallas",
            "Atlanta", "Orlando", "San Diego", "Philadelphia", "Austin", "Phoenix",
            "Portland", "New Orleans", "Toronto", "Vancouver", "Montreal", "Ottawa",
            "Calgary", "Edmonton", "London", "Paris", "Berlin", "Rome", "Madrid",
            "Barcelona", "Amsterdam", "Vienna", "Prague", "Budapest", "Tokyo", "Seoul",
            "Beijing", "Shanghai", "Hong Kong", "Bangkok", "Sydney", "Melbourne",
            "Auckland", "Cape Town", "Johannesburg", "Rio de Janeiro", "Sao Paulo",
            "Buenos Aires", "Naples"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            citySuggestions
        )
        val autoCompleteTextView =
            findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.addTextChangedListener { text ->
            adapter.filter.filter(text)
        }

        autoCompleteTextView.addTextChangedListener { editable ->
            destination = editable.toString()
            if (travel != null && travel.isNotEmpty())
                travel[cont].destination = destination
        }

        editTextDate = findViewById(R.id.FirstData)
        editTextDate2 = findViewById(R.id.SecondData)

        calendar1 = Calendar.getInstance()
        calendar2 = Calendar.getInstance()

        editTextDate.setOnClickListener {
            showDatePickerDialog(editTextDate, calendar1, Calendar.getInstance()) { selectedCalendar ->
                calendar1.time = selectedCalendar.time
                editTextDate2.setOnClickListener {
                    showDatePickerDialog(editTextDate2, calendar2, calendar1) { selectedCalendar2 ->
                        calendar2.time = selectedCalendar2.time
                        updateButtonNextTextColor()
                    }
                }
            }
        }

        editTextDate.addTextChangedListener { editable ->
            startdate = editable.toString()
        }
        editTextDate2.addTextChangedListener { editable ->
            enddate = editable.toString()
        }

        val nextButton: Button = findViewById(R.id.button)
        nextButton.setOnClickListener {
            if (enddate != "") {
                if (travel != null && travel.isNotEmpty()) {
                    travel[cont].datapartenza = startdate
                    travel[cont].dataritorno = enddate
                }


                val intent = Intent(this, calcoliActivity::class.java)

                startActivity(intent)
            }
        }
    }

    private fun showDatePickerDialog(
        editText: EditText,
        calendar: Calendar,
        minDateCalendar: Calendar,
        onDateSet: (Calendar) -> Unit
    ) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            R.style.DatePickerTheme,
            { _, year, month, dayOfMonth ->
                val selectedDate =
                    "${formatTwoDigits(dayOfMonth)}/${formatTwoDigits(month + 1)}/$year"
                editText.setText(selectedDate)
                calendar.set(year, month, dayOfMonth)
                onDateSet(calendar)
            },
            year,
            month,
            day
        )

        datePickerDialog.datePicker.minDate = minDateCalendar.timeInMillis

        datePickerDialog.show()
    }

    private fun formatTwoDigits(num: Int): String {
        return if (num >= 10) num.toString() else "0$num"
    }

    private fun updateButtonNextTextColor() {
        val btnNext: Button = findViewById(R.id.button)
        btnNext.setTextColor(ContextCompat.getColor(this, android.R.color.black))
    }


}
