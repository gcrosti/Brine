package com.gdc.recipebook.data

import com.gdc.recipebook.database.*
import com.gdc.recipebook.database.dataclasses.Image
import com.gdc.recipebook.database.dataclasses.Meal
import com.gdc.recipebook.database.dataclasses.MealFunction
import com.gdc.recipebook.database.dataclasses.MealWithRelations

class FakeRepository(): RepositoryInterface {

    lateinit var mealWithRelations: MealWithRelations
    var testNewMeal = false

    override suspend fun getMealIdFromLocal(name: String): ResultFromGetMealId {
        return ResultFromGetMealId(
            isNew = testNewMeal,
            mealId = mealWithRelations.meal.mealId)
    }

    override suspend fun getMealWithRelationsFromLocal(mealName: String): MealWithRelations {
        return mealWithRelations
    }

    override suspend fun saveMealWithRelations(
        meal: Meal,
        functions: MealFunction?,
        imagesFromEditor: ImagesFromEditor?,
        resourcesFromEditor: ResourcesFromEditor?
    ) {
        mealWithRelations = MealWithRelations(meal)
        functions?.let {
            mealWithRelations.functions = functions
        }
        imagesFromEditor?.let {
            val deletions = findImagesForDeletion(it)
            val insertions = findImagesForInsertion(it)

            deletions?.let {
                for (image in it) {
                    mealWithRelations.images?.remove(image)
                }
            }

            insertions?.let {
                for (image in it) {
                    image.imageMealId = meal.mealId
                    mealWithRelations.images?.add(image)
                }
            }
        }
    }

    override suspend fun deleteMealWithRelations(
        meal: Meal?,
        functions: MealFunction?,
        images: List<Image>?
    ) {
        TODO("Not yet implemented")
    }

    override fun isMealNameTaken(name: String): Boolean {
        return testNewMeal
    }
}