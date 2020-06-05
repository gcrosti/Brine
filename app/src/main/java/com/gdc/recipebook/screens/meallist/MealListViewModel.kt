package com.gdc.recipebook.screens.meallist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdc.recipebook.database.RoomDatabaseDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MealListViewModel(dataSource: RoomDatabaseDAO, application: Application): ViewModel() {

    val database = dataSource

    //private val viewModelJob = Job()

    //private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val meals = database.getAllMeals()

    private var _showNewMealDialog = MutableLiveData<Boolean>(false)

    val showMealDialog: LiveData<Boolean>
        get() = _showNewMealDialog


    //fun onNewMeal()

}