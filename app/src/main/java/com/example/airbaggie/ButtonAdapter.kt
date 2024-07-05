package com.example.airbaggie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ButtonAdapter(
    private val context: Context,
    private val items: List<String>,
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<ButtonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mybaggie_layout, parent, false)
        return ButtonViewHolder(view)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        val item = items[position]
        holder.button.text = item
        holder.button.setOnClickListener {
            onItemClicked(position)
            val intent = Intent(context, showBaggieActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

