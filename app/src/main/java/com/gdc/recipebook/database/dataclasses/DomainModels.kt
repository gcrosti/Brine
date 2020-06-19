package com.gdc.recipebook.database.dataclasses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class MealWithRelations(
    var meal: MutableLiveData<Meal>,
    var images: MutableList<Image> = mutableListOf(),
    var functions: MutableLiveData<MealFunction> = MutableLiveData<MealFunction>(MealFunction(functionMealId = meal.value!!.mealId)),
    var resources: MutableLiveData<MutableList<Resource>> = MutableLiveData(mutableListOf())
)