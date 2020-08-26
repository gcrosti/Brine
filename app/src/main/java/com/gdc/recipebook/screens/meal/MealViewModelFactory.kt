package com.gdc.recipebook.screens.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gdc.recipebook.database.Repository

class MealViewModelFactory (private val repository: Repository): ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MealViewModel::class.java)) {
                return MealViewModel(repository) as T }
            throw IllegalArgumentException()
        }
}