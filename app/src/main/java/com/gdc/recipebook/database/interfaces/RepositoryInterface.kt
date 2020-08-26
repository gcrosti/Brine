package com.gdc.recipebook.database.interfaces

import com.gdc.recipebook.database.dataclasses.*

interface RepositoryInterface {

    suspend fun getRoomMealId(name:String): ResultFromGetMealId

    suspend fun getMealWithRelationsFromLocal(mealName: String): MealWithRelations

    suspend fun saveMealWithRelations(meal: Meal,
                                      functions: MealFunction? = null,
                                      imagesFromEditor: ImagesFromEditor? = null,
                                      resourcesFromEditor: ResourcesFromEditor? = null)

    suspend fun deleteMealWithRelations( meal: Meal? = null,
                                         functions: MealFunction? = null,
                                         images: List<Image>? = null)

    fun isMealNameTaken(name:String): Boolean
}


class ResultFromGetMealId(var isNew: Boolean, var mealId: Long)

class ImagesFromEditor(
    var loadedImages: List<Image> = listOf(),
    var savedImages: MutableList<Image> = mutableListOf()
)

class ResourcesFromEditor(
    var loadedResources: List<Resource> = listOf(),
    var savedResources: MutableList<Resource> = mutableListOf()
)