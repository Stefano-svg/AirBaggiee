package com.example.airbaggie

import Travel
import TravelRepository
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.koin.android.ext.android.inject


class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)
        val travelRepository: TravelRepository by inject()

        var typeoftravel: String = ""

        var travel = travelRepository.travel
        var cont = travelRepository.cont
        Log.d("cont:",travelRepository.cont.toString())
        if (travel != null && travel.isNotEmpty() && cont < travel.size) {
            val travelEdit = travel[cont]

            val btnFormal: Button = findViewById(R.id.button)
            btnFormal.setOnClickListener {
                typeoftravel = "Formal"
                travelEdit.tipoviaggio = typeoftravel
                updateButtonNextTextColor()
            }

            val btnCasual: Button = findViewById(R.id.button2)
            btnCasual.setOnClickListener {
                typeoftravel = "Casual"
                travelEdit.tipoviaggio = typeoftravel
                updateButtonNextTextColor()
            }

            val btnNext: Button = findViewById(R.id.button3)
            btnNext.setOnClickListener {
                if (typeoftravel != "") {
                    val intent = Intent(this, ChooseActivity::class.java)

                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Select a travel type first", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "No travel data found.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateButtonNextTextColor() {
        val btnNext: Button = findViewById(R.id.button3)
        btnNext.setTextColor(ContextCompat.getColor(this, android.R.color.black))
    }


}
