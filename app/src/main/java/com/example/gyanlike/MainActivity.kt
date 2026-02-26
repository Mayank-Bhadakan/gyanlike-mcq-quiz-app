package com.example.gyanlike

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.selects.SelectInstance


class MainActivity : Application() {

    companion object{
       lateinit var instance: MainActivity

    }

    override fun onCreate() {
        super.onCreate()

        Fresco.initialize(this)
        instance = this

    }

}