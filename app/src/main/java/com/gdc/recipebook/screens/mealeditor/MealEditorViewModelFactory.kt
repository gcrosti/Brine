package com.gdc.recipebook.screens.mealeditor

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gdc.recipebook.database.RoomDatabaseDAO

class MealEditorViewModelFactory(): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealEditorViewModel::class.java)) {
            return MealEditorViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}