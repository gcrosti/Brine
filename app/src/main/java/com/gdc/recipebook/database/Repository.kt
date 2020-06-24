package com.gdc.recipebook.database

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.gdc.recipebook.database.dataclasses.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val mealsDatabase: RoomDatabaseDAO) {

    suspend fun setMealId(name: String): Long {
        return withContext(Dispatchers.IO) {
            val meal = Meal(name = name)
            mealsDatabase.insertMeal(meal)
        }
    }

    suspend fun saveMealWithRelations(meal: Meal? = null,
                                      functions: MealFunction? = null,
                                      images: List<Image>? = null) {
        withContext(Dispatchers.IO) {
            meal?.let {
                mealsDatabase.insertMeal(it)
            }

            functions?.let {
                mealsDatabase.insertFunction(it)
            }

            images?.let {
                for (image in it) {
                    mealsDatabase.insertImage(image)
                }
            }
        }
    }


    suspend fun getAllMealsWithFunctionsFromDatabase(): MutableList<MealWithFunctions>? {
        return withContext(Dispatchers.IO) {
            var data = mealsDatabase.getAllMealsWithFunctions() as MutableList<MealWithFunctions>?
            if (data.isNullOrEmpty()) {
                data = null
            }

            data
        }
    }


    suspend fun deleteMealWithRelations(
        meal: Meal? = null,
        functions: MealFunction? = null,
        images: List<Image>? = null
    ) {
        withContext(Dispatchers.IO) {
            meal?.let {
                mealsDatabase.deleteMeal(meal)
            }

            functions?.let {
                mealsDatabase.deleteFunctions(it)
            }

            images?.let {
                for (image in it) {
                    mealsDatabase.deleteImage(image)
                }
            }

        }
    }

    suspend fun retrieveMealWithRelations(name:String): MealWithRelations {
        return withContext(Dispatchers.IO) {
            val meal = mealsDatabase.getMealFromName(name)
            val functions = mealsDatabase.getFunctionsFromId(meal.mealId)
            val images = mealsDatabase.getImagesFromId(meal.mealId)
            val mealWithRelations = MealWithRelations(meal, functions = functions)
            if (images.isNotEmpty()) {
                mealWithRelations.images = images as MutableList<Image>
            }
            mealWithRelations

        }
    }
}