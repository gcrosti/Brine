package com.gdc.recipebook.database

import androidx.core.net.toUri
import com.gdc.recipebook.database.dataclasses.Image
import com.gdc.recipebook.database.dataclasses.Meal
import com.gdc.recipebook.database.dataclasses.MealFunction
import com.gdc.recipebook.database.dataclasses.Resource
import com.google.firebase.database.ktx.database
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class FirebaseDataManager() {

    private val authString = "asdkfjhasdlfaxdoifasfjndls"
    private val database = Firebase.database.getReference(authString)
    private val storage = FirebaseStorage.getInstance()
    private val MEAL = "MEAL"
    private val FUNCTIONS = "FUNCTIONS"
    private val RESOURCES = "RESOURCES"
    private val IMAGES = "IMAGES"

    private val instanceId = FirebaseInstanceId.getInstance().id
    private val databaseInstanceRef = database.child(instanceId)
    private val storageInstanceRef = storage.reference.child(instanceId)


    //MEALS
    fun saveMeal(meal: Meal) {
        databaseInstanceRef.child(meal.mealId.toString()).child(MEAL).setValue(meal)
    }

    //FUNCTIONS
    fun saveFunctions(functions: MealFunction) {
        databaseInstanceRef.child(functions.functionMealId.toString()).child(FUNCTIONS).setValue(functions)
    }

    //RESOURCES
    fun saveResources(mealId: Long, resources: List<Resource>) {
        databaseInstanceRef.child(mealId.toString()).child(RESOURCES)
            .setValue(resources)
    }

    //IMAGES
    fun saveImages(mealId: Long, images: List<Image>) {
        databaseInstanceRef.child(mealId.toString()).child(IMAGES)
            .setValue(images)

        storageInstanceRef.child(mealId.toString()).child(images[0].imageMealId.toString())
            .putFile(images[0].imageURL.toUri())
    }

    //DELETE ALL
    fun deleteMeal(meal: Meal) {
        databaseInstanceRef.child(meal.mealId.toString()).removeValue()
    }

}