package com.example.airbaggie

import InputBottomSheetDialogFragment
import Travel
import TravelRepository
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class calcoliActivity : AppCompatActivity(), InputBottomSheetDialogFragment.OnTextSubmitListener {

    private val apiKey = "d88f7874086463beb50094d57e8fe468"
    var clothingItems = mutableListOf<String>()
    lateinit var adapter: ClothingAdapter
    lateinit var travel: Travel
    lateinit var recyclerView: RecyclerView
    var alertDialogShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcoli)
        val travelRepository: TravelRepository by inject()

        var travelArray = travelRepository.travel
        var cont = travelRepository.cont
        Log.d("cont:",cont.toString())
        if (travelArray != null && travelArray.isNotEmpty()) {
            Log.d("data", travelArray[cont].dataritorno.toString())
            travel = travelArray[cont]

            val city = travel.destination
            val startDate = travel.datapartenza
            val endDate = travel.dataritorno
            val typeoftravel = travel.tipoviaggio

            val abitiCasTemp25: Array<String> = arrayOf(
                "Lightweight t-shirts",
                "Tanktop",
                "Shorts",
                "Summer skirts",
                "Light dress",
                "Swimwear"
            )

            val abbiCasTemp15e24: Array<String> = arrayOf(
                "T-shirt",
                "Casual shirts",
                "Jeans",
                "Trousers",
                "Shorts",
                "Light sweaters"
            )

            val abbiCasTemp14emeno: Array<String> = arrayOf(
                "Warm sweaters",
                "Hoodie",
                "Jeans",
                "Casual trousers",
                "Lightweight jacket",
                "T-shirt"
            )

            val abbiFormTemp25: Array<String> = arrayOf(
                "Light shirts",
                "Lightweight blazers",
                "Lightweight elegant trousers",
                "Lightweight suit",
                "Tie",
                "Formal suit"
            )

            val abbiFormTemp15e24: Array<String> = arrayOf(
                "Elegant shirts",
                "Blazer",
                "Jacket",
                "Suit",
                "Tie or bow tie",
                "Elegant trousers"
            )

            val abbiFormTemp14emeno: Array<String> = arrayOf(
                "Elegant shirts",
                "Warm blazers",
                "Warm jacket",
                "Suit",
                "Tie",
                "Elegant trousers"
            )

            val startDate1 = parseDate(travel.datapartenza)
            val returnDate = parseDate(travel.dataritorno)

            var giorniViaggio = calculateDaysBetween(startDate1, returnDate)
            giorniViaggio += 1

            getAverageTemperature(city, startDate, endDate) { averageTemperature ->
                runOnUiThread {
                    val textType: TextView = findViewById(R.id.typeoftravel)
                    textType.text = "Type of travel: $typeoftravel"

                    val textType2: TextView = findViewById(R.id.textView10)
                    textType2.text = "On the days you go there"

                    val textType3: TextView = findViewById(R.id.textView11)
                    val formattedTemperature = String.format("%.1f", averageTemperature)
                    textType3.text = "will be an average temperature of: $formattedTemperature °"

                    when (giorniViaggio.toInt()) {
                        1 -> {
                            travel.quantity.clear()
                            travel.quantity.addAll(listOf(1, 1, 1, 1, 1, 1))
                        }
                        2 -> {
                            travel.quantity.clear()
                            travel.quantity.addAll(listOf(2, 1, 2, 1, 1, 1))
                        }
                        3 -> {
                            travel.quantity.clear()
                            travel.quantity.addAll(listOf(3, 2, 3, 2, 2, 2))
                        }
                        in 4..7 -> {
                            travel.quantity.clear()
                            travel.quantity.addAll(listOf(4, 2, 3, 2, 2, 2))
                        }
                        in 8..9 -> {
                            travel.quantity.clear()
                            travel.quantity.addAll(listOf(6, 4, 5, 4, 3, 3))
                        }
                        in 10..15 -> {
                            travel.quantity.clear()
                            travel.quantity.addAll(listOf(9, 7, 8, 7, 6, 6))
                        }
                        else -> {
                            travel.quantity.clear()
                            travel.quantity.addAll(listOf(4, 0, 0, 0, 0, 0))
                        }
                    }

                    if (travel.tipoviaggio == "Formal") {
                        when (averageTemperature.toInt()) {
                            in 0..14 -> {
                                clothingItems.addAll(abbiFormTemp14emeno)
                            }
                            in 15..24 -> {
                                clothingItems.addAll(abbiFormTemp15e24)
                            }
                            in 25..70 -> {
                                clothingItems.addAll(abbiFormTemp25)
                            }
                        }
                    } else {
                        when (averageTemperature.toInt()) {
                            in 0..14 -> {
                                clothingItems.addAll(abbiCasTemp14emeno)
                            }
                            in 15..24 -> {
                                clothingItems.addAll(abbiCasTemp15e24)
                            }
                            in 25..70 -> {
                                clothingItems.addAll(abitiCasTemp25)
                            }
                        }
                    }

                    val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
                    recyclerView.layoutManager = LinearLayoutManager(this@calcoliActivity)
                    adapter = ClothingAdapter(this@calcoliActivity, clothingItems, travel.quantity.toMutableList())
                    recyclerView.adapter = adapter



                    val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                        0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    ) {
                        override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                        ): Boolean {
                            return false
                        }

                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                            val position = viewHolder.adapterPosition
                            clothingItems.removeAt(position)
                            travel.quantity.removeAt(position)
                            adapter.notifyItemRemoved(position)
                        }
                    })

                    itemTouchHelper.attachToRecyclerView(recyclerView)

                    val showBottomSheet: FloatingActionButton = findViewById(R.id.floatingActionButton2)
                    showBottomSheet.setOnClickListener {
                        val bottomsheet = InputBottomSheetDialogFragment()
                        bottomsheet.onTextSubmitListener = this@calcoliActivity
                        bottomsheet.show(supportFragmentManager, bottomsheet.tag)
                    }

                    val btnSave: Button = findViewById(R.id.button4)
                    btnSave.setOnClickListener {
                        val nonEmptyAbbigliamento = mutableListOf<String>()

                        for ((index, quantita) in travel.quantity.withIndex()) {
                            if (quantita > 0) {
                                nonEmptyAbbigliamento.add(clothingItems[index])
                                Log.d("abb:",{clothingItems[index]}.toString())
                            }
                        }

                        clothingItems = nonEmptyAbbigliamento

                        travel.quantity = travel.quantity.filter { it > 0 }.toMutableList()
                        travel.clothing = clothingItems.toTypedArray()
                        Log.d("destinazione:",travelArray[cont].destination.toString())
                        Log.d("tipo:",travelArray[cont].tipoviaggio.toString())

                        val intent = Intent(this, MainActivity::class.java)

                        startActivity(intent)
                     }
                }
            }
        } else {
            Log.e("calcoliActivity", "No travel data found.")
        }
    }

    private fun getAverageTemperature(city: String, startDate: String, endDate: String, callback: (Double) -> Unit) {
        val weatherService = RetrofitClient.instance.create(WeatherService::class.java)
        val call = weatherService.getWeatherForecast(city, apiKey)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    if (weatherResponse != null && weatherResponse.list.isNotEmpty()) {
                        val temperatureSumKelvin = weatherResponse.list.sumByDouble { it.main.temp }
                        val averageTemperatureKelvin = temperatureSumKelvin / weatherResponse.list.size
                        val averageTemperatureCelsius = averageTemperatureKelvin - 273.15

                        callback(averageTemperatureCelsius)
                    } else {
                        Log.e("WeatherAPI", "Invalid data received from API")
                        callback(Double.NaN)
                    }
                } else {
                    Log.e("WeatherAPI", "Error in API response: ${response.message()}")
                    callback(Double.NaN)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("WeatherAPI", "Error making API call: ${t.message}")
                callback(Double.NaN)
            }
        })
    }

    private fun parseDate(dateString: String): Date? {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            format.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun calculateDaysBetween(startDate: Date?, endDate: Date?): Long {
        if (startDate == null || endDate == null) {
            return 0
        }

        val startCalendar = Calendar.getInstance()
        startCalendar.time = startDate

        val endCalendar = Calendar.getInstance()
        endCalendar.time = endDate

        val diffMillis = endCalendar.timeInMillis - startCalendar.timeInMillis
        return diffMillis / (24 * 60 * 60 * 1000)
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Attention!")
        alertDialogBuilder.setMessage("By changing the quantity of clothing the parameters will not be respected for the days of the trip ")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onTextSubmit(inputText: String) {
        try {
            val quantities = travel.quantity.toMutableList()
            quantities.add(0)
            travel.quantity = quantities

            clothingItems.add(inputText)

            val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(this@calcoliActivity)
            adapter = ClothingAdapter(this@calcoliActivity, clothingItems, travel.quantity.toMutableList())
            recyclerView.adapter = adapter

        } catch (e: Exception) {
            Log.e("onTextSubmit", "Error adding item: ${e.message}")
            Toast.makeText(this, "Error adding item", Toast.LENGTH_SHORT).show()
        }
    }
}
