package com.gdc.recipebook.screens.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gdc.recipebook.database.RoomDatabaseDAO
import com.gdc.recipebook.screens.meallist.MealListViewModel

class WelcomeViewModelFactory(): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
            return WelcomeViewModel() as T }
        throw IllegalArgumentException()
    }
}