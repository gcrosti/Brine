package com.gdc.recipebook.screens.mealeditor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

open class ResourceBroadcastReceiver(): BroadcastReceiver() {
    val KEY_ACTION_SOURCE = "brineactiontaken"
    var url: String? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        url = intent?.data.toString()
        Log.d(KEY_ACTION_SOURCE,url.toString())
        Toast.makeText(context,"is this working",Toast.LENGTH_LONG).show()


    }
}