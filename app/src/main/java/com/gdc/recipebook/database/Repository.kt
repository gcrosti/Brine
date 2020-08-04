package com.gdc.recipebook.database

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gdc.recipebook.database.dataclasses.*
import kotlinx.coroutines.*
import java.util.*

class Repository private constructor(application: Application): RepositoryInterface {

    private var roomDatabaseDAO: RoomDatabaseDAO = MealRoomDatabase.getInstance(application.applicationContext).databaseDAO
    private val repoJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + repoJob)
    private val firebaseDataManager = FirebaseDataManager()

    private var _mealsWithFunctions = MutableLiveData<List<MealWithFunctions>?>()
    val mealsWithFunctions: LiveData<List<MealWithFunctions>?>
        get() = _mealsWithFunctions



    init {
        updateMealsWithFunctions()
    }

    fun updateMealsWithFunctions() {

        uiScope.launch {
            _mealsWithFunctions.value = getAllMealsWithFunctionsFromDatabase()
        }
    }

    private suspend fun getAllMealsWithFunctionsFromDatabase(): List<MealWithFunctions>? {
        return withContext(Dispatchers.IO) {
            var data: List<MealWithFunctions>? = roomDatabaseDAO.getAllMealsWithFunctions()
            if (data.isNullOrEmpty()) {
                data = null
            } else {
                data = data.sortedBy { it.meal.name  }
            }

            data
        }
    }

    //Save Meal
    override suspend fun getMealIdFromLocal(name: String): ResultFromGetMealId {
        return withContext(Dispatchers.IO) {
            var isNew = false
            val mealId: Long
            var meal = roomDatabaseDAO.getMealFromName(name)
            if (meal == null) {
                isNew = true
                meal = Meal(name = name)
                mealId = roomDatabaseDAO.insertMeal(meal)
            } else {
                mealId = meal.mealId
            }
            ResultFromGetMealId(isNew,mealId)
        }
    }

    override suspend fun saveMealWithRelations(meal: Meal,
                                               functions: MealFunction?,
                                               imagesFromEditor: ImagesFromEditor?,
                                               resourcesFromEditor: ResourcesFromEditor?) {

        withContext(Dispatchers.IO) {
            meal.let {
                roomDatabaseDAO.insertMeal(it)
            }

            functions?.let {
                roomDatabaseDAO.insertFunction(it)
            }

            imagesFromEditor?.let {
                val deletions = findImagesForDeletion(it)
                val insertions = findImagesForInsertion(it)
                Log.d("img insertions",insertions.toString())
                Log.d("img deletions",deletions.toString())

                deletions?.let {
                    for (image in it) {
                        roomDatabaseDAO.deleteImage(image)
                    }
                }

                insertions?.let {
                    for (image in it) {
                        image.imageMealId = meal.mealId
                        roomDatabaseDAO.insertImage(image)
                    }
                }
            }

            resourcesFromEditor?.let {
                val deletions = findResourcesForDeletion(it)
                val insertions = findResourcesForInsertion(it)

                deletions?.let {
                    for (resource in it) {
                        roomDatabaseDAO.deleteResource(resource)
                    }
                }

                insertions?.let {
                    for(resource in it) {
                        resource.resourceMealId = meal.mealId
                        roomDatabaseDAO.insertResource(resource)
                    }
                }
            }
        }

        saveMealWithRelationsToRemote(meal.mealId)
    }

    private suspend fun saveMealWithRelationsToRemote(mealId: Long) {
        withContext(Dispatchers.IO) {

            val meal = roomDatabaseDAO.getMealFromId(mealId)
            val resources = roomDatabaseDAO.getResourcesFromId(mealId)
            val functions = roomDatabaseDAO.getFunctionsFromId(mealId)
            val images = roomDatabaseDAO.getImagesFromId(mealId)

            firebaseDataManager.saveMeal(meal)
            firebaseDataManager.saveFunctions(functions)
            firebaseDataManager.saveResources(mealId,resources)
            firebaseDataManager.saveImages(mealId,images)

        }
    }

    override suspend fun getMealWithRelationsFromLocal(mealName:String): MealWithRelations {
        return withContext(Dispatchers.IO) {
            val meal = roomDatabaseDAO.getMealFromName(mealName)
            val functions = roomDatabaseDAO.getFunctionsFromId(meal.mealId)
            val images = roomDatabaseDAO.getImagesFromId(meal.mealId)
            val resources = roomDatabaseDAO.getResourcesFromId(meal.mealId)
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
    override suspend fun deleteMealWithRelations(
        meal: Meal?,
        functions: MealFunction?,
        images: List<Image>?
    ) {
        withContext(Dispatchers.IO) {
            meal?.let {
                roomDatabaseDAO.deleteMeal(meal)
                firebaseDataManager.deleteMeal(meal)

            }

            functions?.let {
                roomDatabaseDAO.deleteFunctions(it)
            }

            images?.let {
                for (image in it) {
                    roomDatabaseDAO.deleteImage(image)
                }
            }
        }

    }

    override fun isMealNameTaken(name: String): Boolean {
        mealsWithFunctions.value?.forEach {
            if (it.meal.name.toLowerCase(Locale.ROOT) == name.toLowerCase(Locale.ROOT)) {
                return true
            }
        }
        return false
    }

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getRepository(app:Application): Repository {
            return INSTANCE?: synchronized(this) {
                Repository(app).also {
                    INSTANCE = it
                }
            }
        }
    }
}