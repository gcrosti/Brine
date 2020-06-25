package com.gdc.recipebook.screens.welcome

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

class WelcomeViewModel(dataSource: RoomDatabaseDAO): ViewModel() {

    val database = dataSource
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val mealRepository = Repository(database)

    init {
        getAllMeals()
    }

    private var _mealsWithFunctions = MutableLiveData<MutableList<MealWithFunctions>?>()
    val mealsWithFunctions: MutableLiveData<MutableList<MealWithFunctions>?>
        get() = _mealsWithFunctions

    private fun getAllMeals() {
        uiScope.launch {
            _mealsWithFunctions.value = mealRepository.getAllMealsWithFunctionsFromDatabase()
        }
    }

    private var _onNewMealClick = MutableLiveData(false)
    val onNewMealClick: LiveData<Boolean>
        get() = _onNewMealClick

    fun onNewMealClick() {
        _onNewMealClick.value = true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}

