package com.gdc.recipebook.screens.mealeditor.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MealEditorViewModelFactory(): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealEditorViewModel::class.java)) {
            return MealEditorViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}