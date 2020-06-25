package com.gdc.recipebook.screens.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gdc.recipebook.database.RoomDatabaseDAO
import com.gdc.recipebook.screens.meallist.MealListViewModel

class WelcomeViewModelFactory (
    private val datasource: RoomDatabaseDAO): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
            return WelcomeViewModel(datasource) as T }
        throw IllegalArgumentException()
    }
}