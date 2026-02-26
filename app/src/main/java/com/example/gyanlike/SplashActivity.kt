package com.example.gyanlike

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.example.gyanlike.databinding.ActivitySplashBinding
import com.example.gyanlike.PreferenceHelper.get
import com.example.gyanlike.PreferenceHelper.set
import com.example.gyanlike.util.Pref
import com.google.android.material.animation.AnimationUtils

@SuppressLint("CustomSplashScreen")
class SplashActivity : CommonActivity() {

    lateinit var binding: ActivitySplashBinding
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private lateinit var connectivityManager: ConnectivityManager
    val SCREEN_TIME = 1500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isInternetAvailable()) {
            // Show toast message
            showToast("No internet connection")
        }

       // networkCheck()
        initView()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun networkCheck() {

        val inflateLayout = findViewById<View>(R.id.networkError)
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this){
            if (it){
                Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show()
                inflateLayout.visibility = View.GONE
            }
            else{
                inflateLayout.visibility = View.VISIBLE
                Toast.makeText(this, "Not connected", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun initView() {

        val symbol = binding.symbolImageView
        val animation = android.view.animation.AnimationUtils.loadAnimation(this , R.anim.symbol_anim)
        symbol.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({

            if (sharedPreferences[USER_LOGIN]!!)
            {
                Pref.setIntValue(Pref.PREF_USER_TYPE, 2)
                startActivity(Intent(this , HomeActivity::class.java))
            }
            else if (sharedPreferences[ADMIN_LOGIN]!!)
            {
                Pref.setIntValue(Pref.PREF_USER_TYPE, 3)
                startActivity(Intent(this , AdminStd::class.java))
            }
            else
            {
                startActivity(Intent(this , LoginActivity::class.java))
            }

        }, SCREEN_TIME.toLong())
    }

}