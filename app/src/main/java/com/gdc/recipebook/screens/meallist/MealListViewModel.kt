package com.gdc.recipebook.screens.meallist

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdc.recipebook.database.Repository
import com.gdc.recipebook.database.RoomDatabaseDAO
import com.gdc.recipebook.database.dataclasses.MealWithFunctions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MealListViewModel(dataSource: RoomDatabaseDAO, application: Application): ViewModel() {

    private var _mealsWithFunctions = MutableLiveData<MutableList<MealWithFunctions>?>()
    val database = dataSource

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val mealRepository = Repository(database)

    init {
        getAllMeals()
    }

    val mealsWithFunctions: MutableLiveData<MutableList<MealWithFunctions>?>
        get() = _mealsWithFunctions


    private var _showNewMealDialog = MutableLiveData(false)

    val showNewMealDialog: LiveData<Boolean>
        get() = _showNewMealDialog


    fun onNewMealClick() {
        _showNewMealDialog.value = true
    }

    private fun getAllMeals() {
        uiScope.launch {
            _mealsWithFunctions.value = mealRepository.getAllMealsWithFunctionsFromDatabase()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}

class WelcomeViewModel(): ViewModel() {

}