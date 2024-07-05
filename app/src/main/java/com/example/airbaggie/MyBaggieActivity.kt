package com.example.airbaggie

import Travel
import TravelRepository
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject


class MyBaggieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mybaggie)
        val travelRepository: TravelRepository by inject()


        val travel = travelRepository.travel
        var cont = travelRepository.cont
        Log.d("cont:",cont.toString())
        if (travel != null && travel.isNotEmpty() && cont >= 0) {
            Log.d("destinazione:",travel[cont].destination.toString())
            Log.d("tipo:",travel[cont].tipoviaggio.toString())
            val destinations = travel.map { it.destination }.toMutableList()
            val recyclerView: RecyclerView = findViewById(R.id.recyclerbaggie)
            recyclerView.layoutManager = LinearLayoutManager(this)
            val adapter = ButtonAdapter(this, destinations) { position ->
                travelRepository.posizione = position
                Log.d("Selected position:", position.toString())
            }
            recyclerView.adapter = adapter
        }
        else{
            val destinationss = "No travel here!"
            val recyclerView: RecyclerView = findViewById(R.id.recyclerbaggie)
            recyclerView.layoutManager = LinearLayoutManager(this)
            val adapter = ButtonAdapterSingle(this, destinationss)
            recyclerView.adapter = adapter
        }
    }
}

