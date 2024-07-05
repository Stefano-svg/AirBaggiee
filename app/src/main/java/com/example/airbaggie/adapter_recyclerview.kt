package com.example.airbaggie

import TravelRepository
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject

class ClothingAdapter(
    private val context: Context,
    private var clothingItems: MutableList<String>,
    private var quantities: MutableList<Int>
) : RecyclerView.Adapter<ClothingAdapter.ClothingViewHolder>() {


    private val travelRepository: TravelRepository by (context as AppCompatActivity).inject()

    inner class ClothingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clothingName: TextView = itemView.findViewById(R.id.textView9)
        val spinnerQuantity: Spinner = itemView.findViewById(R.id.spinner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
        return ClothingViewHolder(view)
    }

    fun clearAdapter() {
        clothingItems.clear()
        quantities.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ClothingViewHolder, position: Int) {
        val item = clothingItems[position]
        holder.clothingName.text = item

        val quantitiesRange = (0..10).toList()
        val adapter = ArrayAdapter(holder.itemView.context, android.R.layout.simple_spinner_item, quantitiesRange)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.spinnerQuantity.adapter = adapter

        holder.spinnerQuantity.setSelection(quantities.getOrElse(position) { 0 })

        holder.spinnerQuantity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                if (position in clothingItems.indices) {
                    quantities[position] = pos
                    val cont = travelRepository.cont
                    val travel = travelRepository.travel[cont]
                    travel.quantity[position] = pos
                    travelRepository.updateTravel(cont, travel)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Niente da fare
            }
        }

        if (!travelRepository.alertDialogShown) {
            showAlertDialog()
            travelRepository.alertDialogShown = true
        }
    }

    override fun getItemCount(): Int {
        return clothingItems.size
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < clothingItems.size) {
            clothingItems.removeAt(position)
            quantities.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
            val cont = travelRepository.cont
            val travel = travelRepository.travel[cont]
            travel.clothing = clothingItems.toTypedArray()
            travel.quantity = quantities
            travelRepository.updateTravel(cont, travel)
        }
    }

    fun addItem(newItem: String, position: Int = clothingItems.size) {
        if (position >= 0 && position <= clothingItems.size) {
            clothingItems.add(position, newItem)
            quantities.add(position, 0)
            notifyItemInserted(position)
            notifyItemRangeChanged(position, itemCount)
            val cont = travelRepository.cont
            val travel = travelRepository.travel[cont]
            travel.clothing = clothingItems.toTypedArray()
            travel.quantity = quantities
            travelRepository.updateTravel(cont, travel)
        }
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Attention!")
        alertDialogBuilder.setMessage("By changing the quantity of clothing the parameters will not be respected for the days of the trip ")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
