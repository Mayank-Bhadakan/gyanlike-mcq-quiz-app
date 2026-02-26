package com.example.gyanlike.customs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.gyanlike.databinding.CustomHeaderBinding

class CustomHeader @JvmOverloads constructor(
    context: Context,
    attrs : AttributeSet? = null,
    defStyleAttr: Int = 0

  ): RelativeLayout(context,attrs, defStyleAttr)   {

      private var binding : CustomHeaderBinding =
          CustomHeaderBinding.inflate(LayoutInflater.from(context) , this, true)

    init {
        binding.btnBack.setOnClickListener {
            if (context is AppCompatActivity)
                context.finish()
        }
    }

    fun get(): CustomHeaderBinding{
        return binding
    }

    fun setTitle(title: Int) {
        binding.txtTitle.text = context.resources.getText(title)
    }

//    fun setSubTitle(subTitle: String){
//        binding.txtSubTitle.text = context.resources.getText(subTitle.length).toString()
//    }

    fun setBtnLeft(boolean: Boolean){
        binding.btnBack.visibility = if (boolean) View.VISIBLE else View.GONE
    }

}