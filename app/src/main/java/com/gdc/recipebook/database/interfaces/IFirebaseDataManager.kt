package com.gdc.recipebook.database.interfaces

import com.gdc.recipebook.database.dataclasses.Image
import com.gdc.recipebook.database.dataclasses.Meal
import com.gdc.recipebook.database.dataclasses.MealFunction
import com.gdc.recipebook.database.dataclasses.Resource

interface IFirebaseDataManager {
    //MEALS
    fun saveMeal(meal: Meal)

    //FUNCTIONS
    fun saveFunctions(functions: MealFunction)

    //RESOURCES
    fun saveResources(mealId: Long, resources: List<Resource>)

    //IMAGES
    fun saveImages(mealId: Long, images: List<Image>)

    //DELETE ALL
    fun deleteMeal(meal: Meal)
}