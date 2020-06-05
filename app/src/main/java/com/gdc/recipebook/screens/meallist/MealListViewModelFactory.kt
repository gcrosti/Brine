package com.gdc.recipebook.screens.meallist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gdc.recipebook.database.RoomDatabaseDAO

class MealListViewModelFactory (
    private val datasource: RoomDatabaseDAO,
    private val application: Application): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MealListViewModel::class.java)) {
        return MealListViewModel(datasource, application) as T }

        throw IllegalArgumentException()
    }
}