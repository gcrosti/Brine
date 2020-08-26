package com.gdc.recipebook.database.interfaces

import com.gdc.recipebook.database.dataclasses.*

interface IRoomDataManager {
    suspend fun getMealsFunctions(): List<MealWithFunctions>?

    suspend fun getRoomMealId(name: String): ResultFromGetMealId

    suspend fun getMealFromId(id: Long): Meal

    suspend fun getResourcesFromId(id: Long): List<Resource>

    suspend fun getMealFromName(name: String): Meal

    suspend fun getFunctionsFromId(id: Long): MealFunction

    suspend fun getImagesFromId(id: Long): List<Image>

    suspend fun insertImages(images: List<Image>)

    suspend fun insertFunctions(functions: MealFunction)

    suspend fun insertMeal(meal: Meal)

    suspend fun insertResources(resources: List<Resource>)

    suspend fun deleteResources(resources: List<Resource>)

    suspend fun deleteImages(images: List<Image>)

    suspend fun deleteMeal(meal: Meal)

    suspend fun deleteFunctions(functions: MealFunction)
}