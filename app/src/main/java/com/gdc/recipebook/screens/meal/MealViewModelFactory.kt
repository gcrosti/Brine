package com.gdc.recipebook.screens.meal

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gdc.recipebook.database.RoomDatabaseDAO
import com.gdc.recipebook.screens.meallist.MealListViewModel

class MealViewModelFactory (): ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MealViewModel::class.java)) {
                return MealViewModel() as T }
            throw IllegalArgumentException()
        }
}