package com.gdc.recipebook.data

import com.gdc.recipebook.database.dataclasses.*
import com.gdc.recipebook.database.interfaces.IRoomDataManager
import com.gdc.recipebook.database.interfaces.ResultFromGetMealId

class FakeRoomDataManager(private val data: MealWithRelations,
                          private var newMealTest: Boolean = false): IRoomDataManager {

    override suspend fun getMealsFunctions(): List<MealWithFunctions>? {
        return listOf(MealWithFunctions(data.meal,data.functions))
    }

    override suspend fun getRoomMealId(name: String): ResultFromGetMealId {
        return ResultFromGetMealId(newMealTest,data.meal.mealId)
    }

    override suspend fun getMealFromId(id: Long): Meal {
        return data.meal
    }

    override suspend fun getResourcesFromId(id: Long): List<Resource> {
        return data.resources as List<Resource>
    }

    override suspend fun getMealFromName(name: String): Meal {
        return data.meal
    }

    override suspend fun getFunctionsFromId(id: Long): MealFunction {
        return data.functions!!
    }

    override suspend fun getImagesFromId(id: Long): List<Image> {
        return data.images as List<Image>
    }

    override suspend fun insertImages(images: List<Image>) {
        data.images?: mutableListOf()
        for (image in images) {
            data.images!!.add(image)
        }
    }

    override suspend fun insertFunctions(functions: MealFunction) {
        data.functions = functions
    }

    override suspend fun insertMeal(meal: Meal) {
        data.meal = meal
    }

    override suspend fun insertResources(resources: List<Resource>) {
        data.resources = resources as MutableList<Resource>
    }

    override suspend fun deleteResources(resources: List<Resource>) {
        resources.forEach {
            data.resources?.remove(it)
        }
    }

    override suspend fun deleteImages(images: List<Image>) {
        images.forEach {
            data.images?.remove(it)
        }
    }

    override suspend fun deleteMeal(meal: Meal) {

    }

    override suspend fun deleteFunctions(functions: MealFunction) {
        data.functions = null
    }
}