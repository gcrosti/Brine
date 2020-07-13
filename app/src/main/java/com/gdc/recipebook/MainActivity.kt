package com.gdc.recipebook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.gdc.recipebook.database.Repository
import kotlinx.android.synthetic.main.view_menu.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //INITIALIZE LAYOUT
        setContentView(R.layout.activity_main)

        val navController = Navigation.findNavController(this, R.id.fragment)
        supportActionBar?.let { actionBar ->
            Log.d("found","supportactionbar")
            actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            actionBar.setDisplayHomeAsUpEnabled(false)
            actionBar.setCustomView(R.layout.view_menu)

            actionBar.customView.menuTitle.setOnClickListener {
                navController.navigate(R.id.action_global_recipeListFragment)
            }
        }

        //INITIALIZE REPO
        Repository.setDatabase(applicationContext)
        Repository.setMealsWithFunctions()

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            setIntent(intent)
        }
    }


    private fun navToHome() {
        findNavController(R.id.nav_host_fragment_container).navigate(R.id.action_global_recipeListFragment)
        Log.d("navigating","to home")
    }
}
