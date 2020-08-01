package com.gdc.recipebook.screens.meallist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MealListViewModel(): ViewModel() {

    private var _showNewMealDialog = MutableLiveData(false)

    val showNewMealDialog: LiveData<Boolean>
        get() = _showNewMealDialog

    fun onNewMealClick() {
        _showNewMealDialog.value = true
    }
}