package com.gdc.recipebook.database

import android.util.Log
import com.gdc.recipebook.database.dataclasses.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val mealsDatabase: RoomDatabaseDAO) {



    suspend fun setMealId(name: String): Pair<Long,Boolean> {
        return withContext(Dispatchers.IO) {
            var isNew = false
            val mealId: Long
            var meal = mealsDatabase.getMealFromName(name)
            if (meal == null) {
                isNew = true
                meal = Meal(name = name)
                mealId = mealsDatabase.insertMeal(meal)
            } else {
                mealId = meal.mealId
            }
            Pair(mealId,isNew)
        }
    }

    suspend fun saveMealWithRelations(meal: Meal? = null,
                                      functions: MealFunction? = null,
                                      images: List<Image>? = null,
                                      loadedResources: List<String>? = null,
                                      savedResources: List<Resource>? = null) {

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

            loadedResources?.let {
                val deletions = savedResources?.let { newResources -> getResourcesForDeletion(it,newResources) }

                deletions?.let {
                    for (url in it) {
                        mealsDatabase.deleteFromResourcesWithUrl(url)
                    }
                }
            }

            savedResources?.let {
                for (resource in it) {
                    mealsDatabase.insertResource(resource)
                }
            }
        }
    }

    private fun getResourcesForDeletion (loaded: List<String>, saved: List<Resource>): List<String>? {
        val deletionsList: MutableList<String> = loaded as MutableList<String>

        saved.let {
            for (resource in it) {
                if (resource.resourceURL in deletionsList) {
                    deletionsList.remove(resource.resourceURL)
                }
            }
        }
        return deletionsList
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
            val resources = mealsDatabase.getResourcesFromId(meal.mealId)
            val mealWithRelations = MealWithRelations(meal)

            functions?.let {
                mealWithRelations.functions = it
            }

            if (images.isNotEmpty()) {
                mealWithRelations.images = images as MutableList<Image>
            }

            if (resources.isNotEmpty()) {
                mealWithRelations.resources = resources as MutableList<Resource>
            }

            mealWithRelations

        }
    }


}