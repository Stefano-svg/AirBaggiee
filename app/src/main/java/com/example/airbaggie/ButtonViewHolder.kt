package com.example.airbaggie

import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class ButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val button: Button = view.findViewById(R.id.button_item)
}