package com.gdc.recipebook.screens.mealeditor

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdc.recipebook.database.RoomDatabaseDAO
import com.gdc.recipebook.database.dataclasses.*
import kotlinx.coroutines.*

class MealEditorViewModel(dataSource: RoomDatabaseDAO,application: Application): ViewModel() {

    val database = dataSource
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var meals = MutableLiveData<MutableList<MealWithRelations>>()
    init {
        getAllMeals()
    }

    private var _mealWithRelations = MutableLiveData<MealWithRelations>()
    private var _image = MutableLiveData<Image>(_mealWithRelations.value?.images?.get(0))
    private var _onNewImageClick = MutableLiveData(false)
    private var _onSaveMealClick = MutableLiveData(false)
    private var _onDeleteMealClick = MutableLiveData(false)

    val mealWithRelations: LiveData<MealWithRelations>
        get() = _mealWithRelations

    val image: LiveData<Image>
        get() = _image

    val onNewImageClick: LiveData<Boolean>
        get() = _onNewImageClick

    val onSaveMealClick: LiveData<Boolean>
        get() = _onSaveMealClick

    val onDeleteMealClick: LiveData<Boolean>
        get() = _onDeleteMealClick

    private fun getAllMeals() {
        uiScope.launch {
            meals.value = getAllMealsFromDatabase()
        }
    }

    private suspend fun getAllMealsFromDatabase(): MutableList<MealWithRelations> {
        return withContext(Dispatchers.IO) {
            val data = database.getAllMeals() as MutableList<MealWithRelations>
            val functions = database.getAllFunctions()
            Log.d("all meals", data.toString())
            Log.d("all functions", functions.toString())

            data
        }
    }


    fun setMealFromArg(name: String) {
        _mealWithRelations.value = MealWithRelations(MealWithResources(MealWithFunctions(Meal(name = name),MealFunction())))
    }

    fun addNewImage(url: String) {
        _image.value?.imageURL = url
    }

    fun onNewImageClick() {
        _onNewImageClick.value = true
    }

    fun onSaveMealClick() {
        _onSaveMealClick.value = true
    }

    fun onDeleteMealClick() {
        _onDeleteMealClick.value = true
    }


    fun onSave() {
        uiScope.launch {
            mealWithRelations.value?.let { saveMeal(it) }
            //Log.d("meal saved. Name:", meal.value?.mealWithResources?.meal?.name)
        }

    }

    fun onDelete() {
        uiScope.launch {
            mealWithRelations.value?.let { deleteMeal(it) }
        }
    }

    private suspend fun saveMeal(meal:MealWithRelations) {
        withContext(Dispatchers.IO) {
            database.insertMeal(meal.mealWithResources.mealWithFunctions.meal)
            database.insertFunction(meal.mealWithResources.mealWithFunctions.mealFunctions)
        }
    }

    private suspend fun deleteMeal(meal:MealWithRelations) {
        withContext(Dispatchers.IO) {
            database.deleteMeal(meal.mealWithResources.mealWithFunctions.meal)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}