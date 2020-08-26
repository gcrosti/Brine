package com.gdc.recipebook.screens.meallist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gdc.recipebook.database.Repository

class MealListViewModelFactory (private val repository: Repository): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MealListViewModel::class.java)) {
        return MealListViewModel() as T }

        throw IllegalArgumentException()
    }
}