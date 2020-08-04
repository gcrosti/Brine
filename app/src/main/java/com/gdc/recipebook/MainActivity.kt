package com.gdc.recipebook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.view_menu.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navController = Navigation.findNavController(this, R.id.fragment)
        supportActionBar?.let { actionBar ->
            actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            actionBar.setDisplayHomeAsUpEnabled(false)
            actionBar.setCustomView(R.layout.view_menu)

            actionBar.customView.menuTitle.setOnClickListener {
                navController.navigate(R.id.action_global_recipeListFragment)
            }
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            setIntent(intent)
        }
    }
}
