package com.gdc.recipebook.screens.meallist

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdc.recipebook.database.RoomDatabaseDAO
import com.gdc.recipebook.database.dataclasses.Meal
import com.gdc.recipebook.database.dataclasses.MealWithRelations
import kotlinx.coroutines.*

class MealListViewModel(dataSource: RoomDatabaseDAO, application: Application): ViewModel() {

    private var _meals = MutableLiveData<MutableList<MealWithRelations>>()
    val database = dataSource

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        getAllMeals()
    }

    val meals: MutableLiveData<MutableList<MealWithRelations>>
        get() = _meals


    private var _showNewMealDialog = MutableLiveData(false)

    val showNewMealDialog: LiveData<Boolean>
        get() = _showNewMealDialog


    fun onNewMealClick() {
        _showNewMealDialog.value = true
    }

    private fun getAllMeals() {
        uiScope.launch {
            _meals.value = getAllMealsFromDatabase()
            val size = _meals.value!!.size
            Log.d("meals database size:",size.toString())
        }
    }

    private suspend fun getAllMealsFromDatabase(): MutableList<MealWithRelations> {
        return withContext(Dispatchers.IO) {
            val data = database.getAllMeals() as MutableList<MealWithRelations>

            data
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }



}