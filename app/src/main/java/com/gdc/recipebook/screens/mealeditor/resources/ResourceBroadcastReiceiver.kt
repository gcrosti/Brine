package com.gdc.recipebook.screens.mealeditor.resources

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.gdc.recipebook.MainActivity
import com.gdc.recipebook.screens.mealeditor.MealEditorFragment
import com.gdc.recipebook.screens.meallist.MealListFragmentDirections

class ResourceBroadcastReceiver(): BroadcastReceiver() {
    val KEY_ACTION_SOURCE = "brineactiontaken"
    override fun onReceive(context: Context?, intent: Intent?) {
        val uriText = intent?.data.toString()
        Log.d(KEY_ACTION_SOURCE,uriText)
        val onReceiveIntent = Intent(context, MainActivity::class.java)
        onReceiveIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        onReceiveIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        onReceiveIntent.putExtra(Intent.EXTRA_TEXT,uriText)
        context?.startActivity(onReceiveIntent)
    }
}