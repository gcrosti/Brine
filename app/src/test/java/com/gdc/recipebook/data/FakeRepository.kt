package com.gdc.recipebook.data

import com.gdc.recipebook.database.*
import com.gdc.recipebook.database.dataclasses.Image
import com.gdc.recipebook.database.dataclasses.Meal
import com.gdc.recipebook.database.dataclasses.MealFunction
import com.gdc.recipebook.database.dataclasses.MealWithRelations
import com.gdc.recipebook.database.interfaces.ImagesFromEditor
import com.gdc.recipebook.database.interfaces.RepositoryInterface
import com.gdc.recipebook.database.interfaces.ResourcesFromEditor
import com.gdc.recipebook.database.interfaces.ResultFromGetMealId

class FakeRepository():
    RepositoryInterface {

    lateinit var mealWithRelations: MealWithRelations
    var testNewMeal = false

    override suspend fun getRoomMealId(name: String): ResultFromGetMealId {
        return ResultFromGetMealId(
            isNew = testNewMeal,
            mealId = mealWithRelations.meal.mealId
        )
    }

    override suspend fun getMealWithRelationsFromLocal(mealName: String): MealWithRelations {
        return mealWithRelations
        System.out.println("Meal from fake")
        System.out.println(mealWithRelations.meal.toString())
    }

    override suspend fun saveMealWithRelations(
        meal: Meal,
        functions: MealFunction?,
        imagesFromEditor: ImagesFromEditor?,
        resourcesFromEditor: ResourcesFromEditor?
    ) {
        mealWithRelations = MealWithRelations(meal)

        System.out.println("MealAfterFakeSave")
        System.out.println(mealWithRelations.meal.toString())


        functions?.let {
            mealWithRelations.functions = functions
        }
        imagesFromEditor?.let {
            val deletions = findImagesForDeletion(it)
            val insertions = findImagesForInsertion(it)
            System.out.println("Images for insertion from fake")
            System.out.println(insertions.toString())

            deletions?.let {
                for (image in it) {
                    mealWithRelations.images?.remove(image)
                }
            }

            insertions?.let {
                mealWithRelations.images = mutableListOf<Image>()
                for (image in it) {
                    image.imageMealId = meal.mealId
                    mealWithRelations.images?.add(image)
                }
                System.out.println("Images after insertions in fake")
                System.out.println(mealWithRelations.images.toString())
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