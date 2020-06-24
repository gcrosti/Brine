package com.gdc.recipebook.screens.meal

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdc.recipebook.database.Repository
import com.gdc.recipebook.database.RoomDatabaseDAO
import com.gdc.recipebook.database.dataclasses.Image
import com.gdc.recipebook.database.dataclasses.Meal
import com.gdc.recipebook.database.dataclasses.MealFunction
import com.gdc.recipebook.database.dataclasses.MealWithRelations
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
    val thisMeal = MutableLiveData<Meal>(mealWithRelations?.meal)
    val mealImage = MutableLiveData<Image?>(mealWithRelations?.images?.get(0))
    val functions = MutableLiveData<MealFunction>(mealWithRelations?.functions)


    fun setNameFromArg(name: String) {
        mealName = name
        uiScope.launch {
            mealWithRelations = mealRepository.retrieveMealWithRelations(mealName)
            Log.d("retrieved data",mealWithRelations.toString())
            thisMeal.value = mealWithRelations!!.meal
            mealImage.value = mealWithRelations!!.images?.get(0)
            functions.value = mealWithRelations!!.functions
        }
    }


}