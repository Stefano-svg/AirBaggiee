package com.example.airbaggie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClothingDisplayAdapter(
    private val context: Context,
    private var clothingItems: List<String>,
    private var quantities: List<Int>
) : RecyclerView.Adapter<ClothingDisplayAdapter.ClothingViewHolder>() {

    inner class ClothingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clothingName: TextView = itemView.findViewById(R.id.textView9)
        val quantity: TextView = itemView.findViewById(R.id.textViewQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_layoutdisplay, parent, false)
        return ClothingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClothingViewHolder, position: Int) {
        val item = clothingItems[position]
        val quantity = quantities[position]
        holder.clothingName.text = item
        holder.quantity.text = quantity.toString()
    }

    override fun getItemCount(): Int {
        return clothingItems.size
    }
}
