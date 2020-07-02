package com.gdc.recipebook.screens.meal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdc.recipebook.R
import com.gdc.recipebook.database.Repository
import com.gdc.recipebook.database.RoomDatabaseDAO
import com.gdc.recipebook.database.dataclasses.*
import com.gdc.recipebook.screens.meal.resources.ResourceListAdapterMeal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MealViewModel: ViewModel() {

    //SET THREAD SCOPE
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //RETRIEVE DATABASE
    private lateinit var database: RoomDatabaseDAO
    private lateinit var mealRepository: Repository

    fun setDatabase(dataSource: RoomDatabaseDAO) {
        database = dataSource
        mealRepository = Repository(dataSource)
    }

    //RETRIEVE THE RELEVANT DATA
    private var mealName = ""
    private var mealWithRelations: MealWithRelations? = null

    //PUBLIC DATA FOR DISPLAY
    val thisMeal = MutableLiveData(Meal(mealId = 0L, name = mealName))
    val mealImage = MutableLiveData<Image?>(mealWithRelations?.images?.get(0))
    val functions = MutableLiveData<MealFunction>(mealWithRelations?.functions)
    val resources = MutableLiveData<List<Resource>>(mealWithRelations?.resources)
    val adapter = ResourceListAdapterMeal()


    // LOAD DATA
    fun setNameFromArg(name: String) {
        mealName = name
        uiScope.launch {
            mealWithRelations = mealRepository.retrieveMealWithRelations(mealName)
            thisMeal.value = mealWithRelations!!.meal
            mealImage.value = mealWithRelations!!.images?.get(mealWithRelations!!.images!!.lastIndex)
            functions.value = mealWithRelations!!.functions
            resources.value = mealWithRelations!!.resources

            if (thisMeal.value!!.notes.isBlank()) {
                thisMeal.value!!.notes = defaultNotes
            }
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

    //DEFAULT NOTES TEXT
    private val defaultNotes = "This meal has no notes."

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}