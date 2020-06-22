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

/*
class MyBroadcastReceiver(private val resultCatcher: MutableLiveData<String>): ResourceBroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        val onReceiveIntent = Intent(context, MainActivity::class.java)
        onReceiveIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        onReceiveIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(onReceiveIntent)
        if (intent != null) {
            resultCatcher.value = intent.data.toString()
            Log.d("result caught",resultCatcher.value)
        }
    }
} */


