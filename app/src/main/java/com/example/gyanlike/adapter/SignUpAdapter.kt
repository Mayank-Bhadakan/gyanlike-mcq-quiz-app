package com.example.gyanlike.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gyanlike.Model.SignUpModel
import com.example.gyanlike.databinding.ActivitySignUpactivityBinding

class SignUpAdapter (val context: Context, var model: ArrayList<SignUpModel>):RecyclerView.Adapter<SignUpAdapter.ViewHolder> () {

    class ViewHolder (val binding: ActivitySignUpactivityBinding):RecyclerView.ViewHolder(binding.root) {    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ActivitySignUpactivityBinding.inflate(LayoutInflater.from(context) , parent , false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { }

    override fun getItemCount(): Int {
        return model.size
    }
}