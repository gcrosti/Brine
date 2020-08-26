package com.gdc.recipebook.database

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gdc.recipebook.database.dataclasses.*
import com.gdc.recipebook.database.interfaces.*
import kotlinx.coroutines.*
import java.util.*

class Repository(
    private val roomDataManager: IRoomDataManager,
    private val firebaseDataManager: FirebaseDataManager = FirebaseDataManager(),
    private val uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main + Job())):
    RepositoryInterface {


    private var _mealsWithFunctions = MutableLiveData<List<MealWithFunctions>?>()
    val mealsWithFunctions: LiveData<List<MealWithFunctions>?>
        get() = _mealsWithFunctions



    init {
        updateMealsWithFunctions()
    }

    fun updateMealsWithFunctions() {
        uiScope.launch {
            _mealsWithFunctions.value = roomDataManager.getMealsFunctions()
        }
    }

    override suspend fun getRoomMealId(name:String): ResultFromGetMealId {
        return roomDataManager.getRoomMealId(name)
    }

    override suspend fun saveMealWithRelations(meal: Meal,
                                               functions: MealFunction?,
                                               imagesFromEditor: ImagesFromEditor?,
                                               resourcesFromEditor: ResourcesFromEditor?) {

        roomDataManager.insertMeal(meal)

        functions?.let {
            roomDataManager.insertFunctions(it)
        }

        imagesFromEditor?.let {
            val deletions = findImagesForDeletion(it)
            val insertions = findImagesForInsertion(it)

            deletions?.let {images ->
                roomDataManager.deleteImages(images)
            }

            insertions?.let { images ->
                roomDataManager.insertImages(images)
            }
        }

        resourcesFromEditor?.let {
            val deletions = findResourcesForDeletion(it)
            val insertions = findResourcesForInsertion(it)

            deletions?.let { resources ->
                roomDataManager.deleteResources(resources)
            }

            insertions?.let { resources ->
                roomDataManager.insertResources(resources)
            }
        }

        saveMealWithRelationsToRemote(meal.mealId)
    }

    private suspend fun saveMealWithRelationsToRemote(mealId: Long) {
        val meal = roomDataManager.getMealFromId(mealId)
        val resources = roomDataManager.getResourcesFromId(mealId)
        val functions = roomDataManager.getFunctionsFromId(mealId)
        val images = roomDataManager.getImagesFromId(mealId)

        firebaseDataManager.saveMeal(meal)
        firebaseDataManager.saveFunctions(functions)
        firebaseDataManager.saveResources(mealId,resources)
        firebaseDataManager.saveImages(mealId,images)
    }

    override suspend fun getMealWithRelationsFromLocal(mealName:String): MealWithRelations {
        val meal = roomDataManager.getMealFromName(mealName)
        val functions = roomDataManager.getFunctionsFromId(meal.mealId)
        val images = roomDataManager.getImagesFromId(meal.mealId)
        val resources = roomDataManager.getResourcesFromId(meal.mealId)
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

        return mealWithRelations
    }

    //Delete meal
    override suspend fun deleteMealWithRelations(
        meal: Meal?,
        functions: MealFunction?,
        images: List<Image>?
    ) {

            meal?.let {
                roomDataManager.deleteMeal(meal)
                firebaseDataManager.deleteMeal(meal)

            }

            functions?.let {
                roomDataManager.deleteFunctions(it)
            }

            images?.let {
                roomDataManager.deleteImages(it)
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
                val roomDatabaseDAO: RoomDatabaseDAO = MealRoomDatabase.getInstance(app.applicationContext).databaseDAO
                Repository(RoomDataManager(roomDatabaseDAO)).also {
                    INSTANCE = it
                }
            }
        }
    }
}