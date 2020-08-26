package com.gdc.recipebook.database

import com.gdc.recipebook.database.dataclasses.*
import com.gdc.recipebook.database.interfaces.IRoomDataManager
import com.gdc.recipebook.database.interfaces.ResultFromGetMealId
import com.gdc.recipebook.database.interfaces.RoomDatabaseDAO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomDataManager(private val roomDAO: RoomDatabaseDAO,
                      private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) :
    IRoomDataManager {


    override suspend fun getMealsFunctions(): List<MealWithFunctions>? {
        return withContext(ioDispatcher) {
            var data: List<MealWithFunctions>? = roomDAO.getAllMealsWithFunctions()
            data = if (data.isNullOrEmpty()) {
                null
            } else {
                data.sortedBy { it.meal.name  }
            }

            data
        }
    }

    override suspend fun getRoomMealId(name: String): ResultFromGetMealId {
        return withContext(ioDispatcher) {
            var isNew = false
            val mealId: Long
            var meal = roomDAO.getMealFromName(name)
            if (meal == null) {
                isNew = true
                meal = Meal(name = name)
                mealId = roomDAO.insertMeal(meal)
            } else {
                mealId = meal.mealId
            }
            ResultFromGetMealId(
                isNew,
                mealId
            )
        }
    }

    override suspend fun getMealFromId(id: Long): Meal {
        return withContext(ioDispatcher) {
            roomDAO.getMealFromId(id)
        }
    }

    override suspend fun getResourcesFromId(id: Long): List<Resource> {
        return withContext(ioDispatcher) {
            roomDAO.getResourcesFromId(id)
        }
    }

    override suspend fun getMealFromName(name: String): Meal {
        return withContext(ioDispatcher) {
            roomDAO.getMealFromName(name)
        }
    }

    override suspend fun getFunctionsFromId(id: Long): MealFunction {
        return withContext(ioDispatcher) {
            roomDAO.getFunctionsFromId(id)
        }
    }

    override suspend fun getImagesFromId(id: Long): List<Image> {
        return withContext(ioDispatcher) {
            roomDAO.getImagesFromId(id)
        }
    }

    override suspend fun insertImages(images: List<Image>) {
        withContext(ioDispatcher) {
            for (image in images) {
                roomDAO.insertImage(image)
            }
        }
    }

    override suspend fun insertFunctions(functions: MealFunction) {
        withContext(ioDispatcher) {
            roomDAO.insertFunction(functions)
        }
    }

    override suspend fun insertMeal(meal: Meal) {
        withContext(ioDispatcher) {
            roomDAO.insertMeal(meal)
        }
    }

    override suspend fun insertResources(resources: List<Resource>) {
        withContext(ioDispatcher) {
            for (resource in resources) {
                roomDAO.insertResource(resource)
            }
        }
    }

    override suspend fun deleteResources(resources:List<Resource>) {
        withContext(ioDispatcher) {
            for (resource in resources) {
                roomDAO.deleteResource(resource)
            }
        }
    }

    override suspend fun deleteImages(images: List<Image>) {
        withContext(ioDispatcher) {
            for (image in images) {
                roomDAO.deleteImage(image)
            }
        }
    }

    override suspend fun deleteMeal(meal: Meal) {
        withContext(ioDispatcher) {
            roomDAO.deleteMeal(meal)
        }
    }

    override suspend fun deleteFunctions(functions: MealFunction) {
        withContext(ioDispatcher) {
            roomDAO.deleteFunctions(functions)
        }
    }
}