package com.example.airbaggie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ButtonAdapterSingle(private val context: Context, private val item: String) :
    RecyclerView.Adapter<ButtonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mybaggie_layout, parent, false)
        return ButtonViewHolder(view)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        holder.button.text = item
        holder.button.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return 1
    }
}

