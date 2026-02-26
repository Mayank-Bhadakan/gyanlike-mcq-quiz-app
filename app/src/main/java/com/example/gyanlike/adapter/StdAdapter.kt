package com.example.gyanlike.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gyanlike.Model.StdModel
import com.example.gyanlike.R

class StdAdapter (var model: ArrayList<StdModel>):RecyclerView.Adapter<StdAdapter.ViewHolder>() {

    class ViewHolder (itemView: View):RecyclerView.ViewHolder(itemView) {

        val standard : TextView = itemView.findViewById(R.id.itemStandard)
        val std : TextView = itemView.findViewById(R.id.itemStd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_std, parent , false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentitem = model[position]

       //holder.standard.text = currentitem.standard
        holder.std.text = currentitem.std

    }

    override fun getItemCount(): Int {
        return model.size
    }
}