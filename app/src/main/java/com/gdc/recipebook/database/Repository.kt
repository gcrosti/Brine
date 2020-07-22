package com.gdc.recipebook.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gdc.recipebook.database.dataclasses.*
import kotlinx.coroutines.*
import java.util.*

object Repository {

    private lateinit var database: RoomDatabaseDAO
    private val repoJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + repoJob)
    private val firebaseDataManager = FirebaseDataManager()

    fun setDatabase(context: Context? = null) {
        context?.let {
            database = MealRoomDatabase.getInstance(it).databaseDAO
        }
    }

    fun setMealsWithFunctions() {
        uiScope.launch {
            _mealsWithFunctions.value = getAllMealsWithFunctionsFromDatabase()
        }
    }

    //Meals With Functions
    private var _mealsWithFunctions = MutableLiveData<List<MealWithFunctions>?>()

    val mealsWithFunctions: LiveData<List<MealWithFunctions>?>
        get() = _mealsWithFunctions

    private suspend fun getAllMealsWithFunctionsFromDatabase(): List<MealWithFunctions>? {
        return withContext(Dispatchers.IO) {
            var data: List<MealWithFunctions>? = database.getAllMealsWithFunctions()
            if (data.isNullOrEmpty()) {
                data = null
            } else {
                data = data.sortedBy { it.meal.name  }
            }

            data
        }
    }

    //Save Meal
    suspend fun setMealId(name: String): Pair<Long,Boolean> {
        return withContext(Dispatchers.IO) {
            var isNew = false
            val mealId: Long
            var meal = database.getMealFromName(name)
            if (meal == null) {
                isNew = true
                meal = Meal(name = name)
                mealId = database.insertMeal(meal)
            } else {
                mealId = meal.mealId
            }
            Pair(mealId,isNew)
        }
    }

    suspend fun saveMealWithRelations(meal: Meal,
                                      functions: MealFunction? = null,
                                      loadedImages: List<String>? = null,
                                      savedImages: List<Image>? = null,
                                      loadedResources: List<String>? = null,
                                      savedResources: List<Resource>? = null) {

        withContext(Dispatchers.IO) {
            meal.let {
                database.insertMeal(it)
            }

            functions?.let {
                database.insertFunction(it)
            }

            loadedImages?.let {
                val deletions = savedImages?.let { newImages -> getImagesForDeletion(it,newImages)}

                deletions?.let {
                    for (url in it) {
                        database.deleteImageFromUrl(url)
                    }
                }
            }

            savedImages?.let {
                for (image in it) {
                    database.insertImage(image)
                }
            }

            loadedResources?.let {
                val deletions = savedResources?.let { newResources -> getResourcesForDeletion(it,newResources) }

                deletions?.let {
                    for (url in it) {
                        database.deleteFromResourcesWithUrl(url)
                    }
                }
            }

            savedResources?.let {
                for (resource in it) {
                    database.insertResource(resource)
                }
            }
        }

        saveMealWithRelationsToRemote(meal.mealId)
    }

    private suspend fun saveMealWithRelationsToRemote(mealId: Long) {
        withContext(Dispatchers.IO) {

            val meal = database.getMealFromId(mealId)
            val resources = database.getResourcesFromId(mealId)
            val functions = database.getFunctionsFromId(mealId)
            val images = database.getImagesFromId(mealId)

            firebaseDataManager.saveMeal(meal)
            firebaseDataManager.saveFunctions(functions)
            firebaseDataManager.saveResources(mealId,resources)
            firebaseDataManager.saveImages(mealId,images)

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

    private fun getImagesForDeletion(loaded: List<String>, saved: List<Image>): List<String>? {
        val deletionList: MutableList<String> = loaded as MutableList<String>

        for (image in saved) {
            if(image.imageURL in deletionList) {
                deletionList.remove(image.imageURL)
            }
        }
        return deletionList
    }


    //Retrieve Meal
    suspend fun retrieveMealWithRelations(name:String): MealWithRelations {
        return withContext(Dispatchers.IO) {
            val meal = database.getMealFromName(name)
            val functions = database.getFunctionsFromId(meal.mealId)
            val images = database.getImagesFromId(meal.mealId)
            val resources = database.getResourcesFromId(meal.mealId)
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



    //Delete meal
    suspend fun deleteMealWithRelations(
        meal: Meal? = null,
        functions: MealFunction? = null,
        images: List<Image>? = null
    ) {
        withContext(Dispatchers.IO) {
            meal?.let {
                database.deleteMeal(meal)
                firebaseDataManager.deleteMeal(meal)
            }

            functions?.let {
                database.deleteFunctions(it)
            }

            images?.let {
                for (image in it) {
                    database.deleteImage(image)
                }
            }

        }
    }
}