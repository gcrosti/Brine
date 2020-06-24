package com.gdc.recipebook.database.dataclasses

import androidx.lifecycle.MutableLiveData

data class MealWithRelations(
    var meal: Meal,
    var images: MutableList<Image>? = null,
    var functions: MealFunction = MealFunction(functionMealId = 0L),
    var resources: MutableList<Resource>? = null
)