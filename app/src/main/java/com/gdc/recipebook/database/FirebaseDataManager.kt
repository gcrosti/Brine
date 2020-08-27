package com.gdc.recipebook.database

import android.util.Log
import androidx.core.net.toUri
import com.gdc.recipebook.database.dataclasses.*
import com.gdc.recipebook.database.interfaces.IFirebaseDataManager
import com.google.firebase.database.ktx.database
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class FirebaseDataManager() :
    IFirebaseDataManager {

    private val authString = FirebaseDBauth.authString
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
    override fun saveMeal(meal: Meal) {
        databaseInstanceRef.child(meal.mealId.toString()).child(MEAL).setValue(meal)
    }

    //FUNCTIONS
    override fun saveFunctions(functions: MealFunction) {
        databaseInstanceRef.child(functions.functionMealId.toString()).child(FUNCTIONS).setValue(functions)
    }

    //RESOURCES
    override fun saveResources(mealId: Long, resources: List<Resource>) {
        databaseInstanceRef.child(mealId.toString()).child(RESOURCES)
            .setValue(resources)
    }

    //IMAGES
    override fun saveImages(mealId: Long, images: List<Image>) {
        databaseInstanceRef.child(mealId.toString()).child(IMAGES)
            .setValue(images)

        storageInstanceRef.child(mealId.toString()).listAll().addOnSuccessListener { listResult ->
            val previousImages = listResult.items
            for (image in previousImages) {
                image.delete().addOnSuccessListener {
                    Log.d("imagedelete", image.toString()) }
                    .addOnFailureListener {
                        Log.d("imagedelete","failed") }
            }
        }

        if (images.isNotEmpty()) {
            for (image in images) {
                Log.d("imageforstorage", image.toString())
                storageInstanceRef.child(mealId.toString()).child(image.imageId.toString())
                    .putFile(image.imageURL.toUri())
            }
        }
    }

    //DELETE ALL
    override fun deleteMeal(meal: Meal) {
        databaseInstanceRef.child(meal.mealId.toString()).removeValue()
    }

}