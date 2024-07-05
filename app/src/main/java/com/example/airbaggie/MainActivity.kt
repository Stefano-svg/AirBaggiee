package com.example.airbaggie

import Travel
import TravelRepository
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

   private val travelRepository: TravelRepository by inject()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        val btnCreateJourney : Button = findViewById(R.id.btnCreateJourney)
        btnCreateJourney.setOnClickListener {
            travelRepository.cont += 1
            addnewtravel()
            val intent = Intent(this,SecondActivity::class.java)

            startActivity(intent)

        }
        val btnMyBaggie : Button = findViewById(R.id.button)
        btnMyBaggie.setOnClickListener {
            val intent = Intent(this,MyBaggieActivity::class.java)

            startActivity(intent)
        }


    }

    fun addnewtravel(){
        val newTravel = Travel(
            datapartenza = "",
            dataritorno = "",
            tipoviaggio = "",
            destination = "",
            clothing = arrayOf("", ""),
            quantity = mutableListOf(0, 0)
        )

        travelRepository.travel.add(newTravel)

    }
}