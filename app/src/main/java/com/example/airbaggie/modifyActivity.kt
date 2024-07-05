package com.example.airbaggie

import InputBottomSheetDialogFragment
import Travel
import TravelRepository
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.ext.android.inject

class modifyActivity : AppCompatActivity(), InputBottomSheetDialogFragment.OnTextSubmitListener {
    private val travelRepository: TravelRepository by inject()
    lateinit var adapter: ClothingAdapter
    var clothingItems = mutableListOf<String>()
    lateinit var travel: Travel
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_modify)
        var travelArray = travelRepository.travel
        var cont = travelRepository.posizione
        if (travelArray != null && travelArray.isNotEmpty()) {
            travel = travelArray[cont]
            val textType: TextView = findViewById(R.id.textView21)
            textType.text = travel.destination

            val textType2: TextView = findViewById(R.id.textView23)
            textType2.text = travel.datapartenza

            val textType3: TextView = findViewById(R.id.textView25)
            textType3.text = travel.dataritorno

            val textType4: TextView = findViewById(R.id.textView27)
            textType4.text = travel.tipoviaggio

            clothingItems.addAll(travel.clothing.toList())
            recyclerView = findViewById(R.id.recyclerviewmodify)
            recyclerView.layoutManager = LinearLayoutManager(this@modifyActivity)
            adapter = ClothingAdapter(this@modifyActivity, clothingItems, travel.quantity.toMutableList())
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
            val showBottomSheet: FloatingActionButton = findViewById(R.id.floatingActionButton3)
            showBottomSheet.setOnClickListener {
                val bottomsheet = InputBottomSheetDialogFragment()
                bottomsheet.onTextSubmitListener = this@modifyActivity
                bottomsheet.show(supportFragmentManager, bottomsheet.tag)
            }
            val btnSave: Button = findViewById(R.id.buttonsavemodify)
            btnSave.setOnClickListener {
                val nonEmptyAbbigliamento = mutableListOf<String>()

                for ((index, quantita) in travel.quantity.withIndex()) {
                    if (quantita > 0) {
                        nonEmptyAbbigliamento.add(clothingItems[index])
                        Log.d("abb:", clothingItems[index])
                    }
                }

                clothingItems = nonEmptyAbbigliamento

                travel.quantity = travel.quantity.filter { it > 0 }.toMutableList()
                travel.clothing = clothingItems.toTypedArray()
                travelRepository.updateTravel(cont, travel)
                Log.d("destinazione:", travelArray[cont].destination.toString())
                Log.d("tipo:", travelArray[cont].tipoviaggio.toString())

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }


        }
    }

    override fun onTextSubmit(inputText: String) {
        try {
            val quantities = travel.quantity.toMutableList()
            quantities.add(0)
            travel.quantity = quantities

            clothingItems.add(inputText)

            val recyclerView: RecyclerView = findViewById(R.id.recyclerviewmodify)
            recyclerView.layoutManager = LinearLayoutManager(this@modifyActivity)
            adapter = ClothingAdapter(this@modifyActivity, clothingItems, travel.quantity.toMutableList())
            recyclerView.adapter = adapter

        } catch (e: Exception) {
            Log.e("onTextSubmit", "Error adding item: ${e.message}")
            Toast.makeText(this, "Error adding item", Toast.LENGTH_SHORT).show()
        }
    }
}
