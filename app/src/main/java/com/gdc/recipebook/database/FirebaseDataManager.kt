package com.gdc.recipebook.database

import android.content.Context
import com.gdc.recipebook.R
import com.gdc.recipebook.database.dataclasses.Meal
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseDataManager(context:Context) {

    private val authString = context.getString(R.string.liveDataAuth)
    private val database = Firebase.database.getReference(authString)

    fun saveMeal(meal: Meal) {
        database.child(meal.instanceId).child(meal.name).setValue(meal)
    }

    fun deleteMeal(meal: Meal) {
        database.child(meal.instanceId).child(meal.name).removeValue()
    }
}