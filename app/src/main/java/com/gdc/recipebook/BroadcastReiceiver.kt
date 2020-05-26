package com.gdc.recipebook

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

open class ResourceBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val url = intent?.dataString
        Log.d("selected url", url)
    }
}