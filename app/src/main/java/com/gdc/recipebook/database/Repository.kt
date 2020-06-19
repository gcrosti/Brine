package com.gdc.recipebook.database

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.gdc.recipebook.database.dataclasses.Meal
import com.gdc.recipebook.database.dataclasses.MealWithFunctions
import com.gdc.recipebook.database.dataclasses.MealWithRelations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val mealsDatabase: RoomDatabaseDAO) {

    suspend fun setMeal(name: String): Long {
        return withContext(Dispatchers.IO) {
            val meal = Meal(name = name)
            mealsDatabase.insertMeal(meal)
        }
    }

    suspend fun saveMealWithRelations(mealWithRelations: MealWithRelations) {
        withContext(Dispatchers.IO) {
            mealsDatabase.insertMeal(mealWithRelations.meal.value!!)
            mealsDatabase.insertFunction(mealWithRelations.functions.value!!)
            if (mealWithRelations.images.isNotEmpty()) {
                mealsDatabase.insertImage(mealWithRelations.images[0])
            }

            if (mealWithRelations.resources.value!!.isNotEmpty()) {
                for (resource in mealWithRelations.resources.value!!) {
                    mealsDatabase.insertResource(resource)
                }
            }
        }
    }

    suspend fun getAllMealsFromDatabase(): MutableList<MealWithFunctions>? {
        return withContext(Dispatchers.IO) {
            var data = mealsDatabase.getAllMealsWithFunctions() as MutableList<MealWithFunctions>?
            if (data.isNullOrEmpty()) {
                data = mutableListOf<MealWithFunctions>()
            }

            data
        }
    }


    suspend fun deleteMealWithRelations(mealWithRelations: MealWithRelations) {
        withContext(Dispatchers.IO) {
            val idtogo = mealWithRelations.meal.value!!.mealId
            mealsDatabase.deleteMeal(mealWithRelations.meal.value!!)
            mealsDatabase.deleteFunctionsFromId(idtogo)
            mealsDatabase.deleteImagesFromId(idtogo)
            mealsDatabase.deleteResourcesFromId(idtogo)
        }
    }

}