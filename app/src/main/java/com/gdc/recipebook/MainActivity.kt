package com.gdc.recipebook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /*
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            val newResourceUri = intent.extras?.getString(Intent.EXTRA_TEXT)
            val fragment = newResourceUri?.let { MealEditorFragment.newInstance(it) }
            fragment?.onResume()
        }
    } */
}
