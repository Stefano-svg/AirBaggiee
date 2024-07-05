package com.example.airbaggie

import Travel
import TravelRepository
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject

class showBaggieActivity: AppCompatActivity() {
    lateinit var adapter: ClothingDisplayAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_showbaggieactivity)
        val travelRepository: TravelRepository by inject()
        var travelArray = travelRepository.travel
        var cont = travelRepository.posizione
        if (travelArray != null && travelArray.isNotEmpty()) {
            val travel = travelArray[cont]

            val textType: TextView = findViewById(R.id.textView15)
            textType.text = travel.destination

            val textType2: TextView = findViewById(R.id.textView28)
            textType2.text =  travel.datapartenza
            val textType3: TextView = findViewById(R.id.textView30)
            textType3.text =  travel.dataritorno

            val textType4: TextView = findViewById(R.id.textView31)
            textType4.text = travel.tipoviaggio

            val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = ClothingDisplayAdapter(this, travel.clothing.toMutableList(), travel.quantity.toMutableList())
            recyclerView.adapter = adapter
            val deleteButton: Button = findViewById(R.id.buttondelete)
            deleteButton.setOnClickListener {
                travelArray.removeAt(cont)

                travelRepository.cont -= 1
                Log.d("cont:", cont.toString())
                val intent = Intent(this,MainActivity::class.java)

                startActivity(intent)
            }
            val modifyButton: Button = findViewById(R.id.buttonmodify)
            modifyButton.setOnClickListener {



                Log.d("cont:", cont.toString())
                val intent = Intent(this,modifyActivity::class.java)

                startActivity(intent)
            }
        }
        }

    }
