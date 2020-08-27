package com.gdc.recipebook.data

import com.gdc.recipebook.database.dataclasses.*
import com.gdc.recipebook.database.interfaces.IFirebaseDataManager

class FakeFirebaseDataManager(private val data: MealWithRelations): IFirebaseDataManager {
    override fun saveMeal(meal: Meal) {
        data.meal = meal
    }

    override fun saveFunctions(functions: MealFunction) {
        data.functions = functions
    }

    override fun saveResources(mealId: Long, resources: List<Resource>) {
        data.resources = resources as MutableList<Resource>
    }

    override fun saveImages(mealId: Long, images: List<Image>) {
        data.images = images as MutableList<Image>
    }

    override fun deleteMeal(meal: Meal) {
        TODO("Not yet implemented")
    }
}