package com.gdc.recipebook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.gdc.recipebook.screens.mealeditor.MealEditorFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            val newResourceUri = intent.extras?.getString(Intent.EXTRA_TEXT)
            val fragment = newResourceUri?.let { MealEditorFragment.newInstance(it) }
            fragment?.onResume()
        }
    }
}
