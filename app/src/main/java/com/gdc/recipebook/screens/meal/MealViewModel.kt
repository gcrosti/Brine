package com.gdc.recipebook.screens.meal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdc.recipebook.database.Repository
import com.gdc.recipebook.database.dataclasses.Meal
import com.gdc.recipebook.database.dataclasses.MealFunction
import com.gdc.recipebook.database.dataclasses.MealWithRelations
import com.gdc.recipebook.database.dataclasses.Resource
import com.gdc.recipebook.screens.meal.resources.ResourceListAdapterMeal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MealViewModel(private val repository: Repository): ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var mealName = ""
    private var mealWithRelations: MealWithRelations? = null

    val thisMeal = MutableLiveData(Meal(mealId = 0L, name = mealName))
    val functions = MutableLiveData<MealFunction>(mealWithRelations?.functions)
    val resources = MutableLiveData<List<Resource>>(mealWithRelations?.resources)
    val images = MutableLiveData(mealWithRelations?.images)

    val adapter = ResourceListAdapterMeal()


    // LOAD DATA
    fun setNameFromArg(name: String) {
        mealName = name
        uiScope.launch {
            mealWithRelations = repository.getMealWithRelationsFromLocal(mealName)
            thisMeal.value = mealWithRelations!!.meal
            images.value = mealWithRelations!!.images
            functions.value = mealWithRelations!!.functions
            resources.value = mealWithRelations!!.resources
        }
    }


    //EDIT MEAL LISTENER
    private var _onEditMealClick = MutableLiveData(false)
    val onEditMealClick: LiveData<Boolean>
        get() = _onEditMealClick
    fun onEditMealClick() {
        _onEditMealClick.value = true
        Log.d("edit meal", "clicked")
    }


    fun onNavigatingToEditMeal () {
        _onEditMealClick.value = false
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}