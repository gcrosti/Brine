package com.gdc.recipebook.screens.meallist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdc.recipebook.database.Repository

class MealListViewModel(): ViewModel() {

    init {
        Repository.setMealsWithFunctions()
    }
    private var _showNewMealDialog = MutableLiveData(false)

    val showNewMealDialog: LiveData<Boolean>
        get() = _showNewMealDialog

    fun onNewMealClick() {
        _showNewMealDialog.value = true
    }
}