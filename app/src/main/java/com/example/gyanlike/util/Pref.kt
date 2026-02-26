package com.example.gyanlike.util


import android.content.Context
import android.content.SharedPreferences
import com.example.gyanlike.MainActivity

object Pref
{
    private var sharedPreferences: SharedPreferences? = null

    val PREF_FILE: String = "QUICK_COACH_PREF"
    private fun openPreference() {

        sharedPreferences =
            MainActivity.instance.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)

    }

    /*Fore String Value Store*/
    fun getStringValue(key: String, defaultValue: String): String? {
        openPreference()
        val result = sharedPreferences!!.getString(key, defaultValue)
        sharedPreferences = null
        return result
    }

    fun setStringValue(key: String, value: String) {
        openPreference()
        val prefsPrivateEditor: SharedPreferences.Editor? = sharedPreferences!!.edit()
        prefsPrivateEditor!!.putString(key, value)
        prefsPrivateEditor.apply()
        sharedPreferences = null
    }


    /*For Integer Value*/

    fun setIntValue(key: String, value: Int) {
        openPreference()
        val prefsPrivateEditor: SharedPreferences.Editor? = sharedPreferences!!.edit()
        prefsPrivateEditor!!.putInt(key, value)
        prefsPrivateEditor.apply()
        sharedPreferences = null
    }

    fun getIntValue(key: String, defaultValue: Int): Int {
        openPreference()
        val result = sharedPreferences!!.getInt(key, defaultValue)
        sharedPreferences = null
        return result
    }


    /*For boolean Value Store*/

    fun getBooleanValue(key: String, defaultValue: Boolean): Boolean {
        openPreference()
        val result = sharedPreferences!!.getBoolean(key, defaultValue)
        sharedPreferences = null
        return result
    }

    fun setBooleanValue(key: String, value: Boolean) {
        openPreference()
        val prefsPrivateEditor: SharedPreferences.Editor? = sharedPreferences!!.edit()
        prefsPrivateEditor!!.putBoolean(key, value)
        prefsPrivateEditor.apply()
    }

    /*For Remove variable from pref*/
    private fun remove(key: String) {
        openPreference()
        val prefsPrivateEditor = sharedPreferences!!.edit()
        prefsPrivateEditor.remove(key)
        prefsPrivateEditor.apply()
        sharedPreferences = null
    }


    /*For Remove variable from pref*/
    fun clearAllPref() {
        remove(PREF_USER_ID)
        remove(PREF_USER_NAME)
        remove(PREF_USER_PIC)
        remove(PREF_USER_EMAIL)
        remove(PREF_USER_PHONE)
        remove(PREF_DEVICE_TOKEN)
        remove(PREF_LANGUAGE)
        remove(PREF_AUTH_TOKEN)
        // remove(PREF_IS_DARK_MODE)
        remove(PREF_USER_TYPE)
        remove(PREF_IS_USER_LOGIN)
        remove(PREF_IS_USER_LOCKED)
        remove(PREF_USER_JERSEY_NO)
        remove(PREF_USER_POSITION)
        remove(PREF_IS_SHOW_INTRODUCTION)


        remove(PREF_PLAN_ID)
        remove(PREF_PLAN_STRING)
        remove(PREF_IS_PAID_USER)
        remove(PREF_TOTAL_COACH_ADVICE_COUNT)
    }


    const val PREF_USER_ID: String = "PREF_USER_ID"
    const val PREF_USER_TYPE: String = "PREF_USER_TYPE"// 3 for player 2 for Coach
    const val PREF_USER_NAME: String = "PREF_USER_NAME"
    const val PREF_REFER_ID: String = "PREF_REFER_ID"
   // var PREF_USER_EMAIL: String = "PREF_USER_EMAIL"
    const val PREF_USER_EMAIL: String = "PREF_USER_EMAIL"
    const val PREF_USER_PHONE: String = "PREF_USER_PHONE"
    const val PREF_USER_PIC: String = "PREF_USER_PIC"
    const val PREF_USER_JERSEY_NO: String = "PREF_USER_JERSEY_NO"
    const val PREF_USER_POSITION: String = "PREF_USER_POSITION"
    const val PREF_DEVICE_TOKEN: String = "PREF_DEVICE_TOKEN"
    const val PREF_LANGUAGE: String = "PREF_LANGUAGE"
    const val PREF_AUTH_TOKEN: String = "PREF_AUTH_TOKEN"
    const val PREF_IS_DARK_MODE: String = "PREF_IS_DARK_MODE"
    const val PREF_IS_USER_LOGIN: String = "PREF_IS_USER_LOGIN"
    const val PREF_IS_SHOW_INTRODUCTION: String = "PREF_IS_SHOW_INTRODUCTION"
    const val PREF_IS_USER_LOCKED: String = "PREF_IS_USER_LOCKED"

    const val PREF_PLAN_ID: String = "PREF_PLAN_ID"
    const val PREF_GENDER: String = "PREF_GENDER"
    const val PREF_PLAN_STRING: String = "PREF_PLAN_STRING"
    const val PREF_IS_PAID_USER: String = "PREF_IS_PAID_USER"
    const val PREF_TOTAL_COACH_ADVICE_COUNT: String = "PREF_TOTAL_COACH_ADVICE_COUNT"

    //BaseActivity Pref...

    const val PREF_CURRENT_LATITUDE: String = "PREF_CURRENT_LATITUDE"
    const val PREF_CURRENT_LONGITUDE: String = "PREF_CURRENT_LONGITUDE"
    const val PREF_IS_SELECT_ADDRESS: String = "PREF_IS_SELECT_ADDRESS"
    const val PREF_LATITUDE: String = "PREF_LATITUDE"
    const val PREF_LONGITUDE: String = "PREF_LONGITUDE"
}