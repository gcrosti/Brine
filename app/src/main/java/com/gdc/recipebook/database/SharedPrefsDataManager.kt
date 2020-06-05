package com.gdc.recipebook.database

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.gdc.recipebook.database.dataclasses.Meal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefsDataManager(context: Context) {
    private val PREF_NAME = "list"
    private var prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME,0)
    private val gson = Gson()

    fun saveList(list: MutableList<Meal> ) {
        val sharedPrefsEditor = prefs.edit()
        val jsonList = gson.toJson(list)
        sharedPrefsEditor.putString(PREF_NAME,jsonList)
        sharedPrefsEditor.apply()

    }

    fun readList(): MutableList<Meal> {
        val mealList: MutableList<Meal>
        val jsonList = prefs.getString(PREF_NAME,"")
        mealList = when {
            jsonList.isNullOrEmpty() -> mutableListOf()
            else -> gson.fromJson(jsonList,object: TypeToken<MutableList<Meal>>() {}.type )
        }
        return mealList
    }

    fun readMeal(mealList: MutableList<Meal>, name: String): Meal? {
        val recipe  = mealList.filter { it.name == name}
        if (recipe.isNullOrEmpty()) {
            return null
        }
        return recipe[0]
    }

    fun editMeal(mealList: MutableList<Meal>, oldRecipeName: String, newMeal: Meal) {
        val isRemoved = mealList.remove(readMeal(mealList,oldRecipeName))
        val isAdded = mealList.add(newMeal)
        Log.d("removed?","$isRemoved")
        Log.d("added?","$isAdded")
    }


    fun convertStringToList(string: String): MutableList<String> {
        val stop = string.length -1
        return string.substring(1,stop).split(", ") as MutableList<String>
    }

    fun deleteMeal(mealList: MutableList<Meal>, name:String) {
        val meal  = mealList.filter { it.name == name}[0]
        mealList.remove(meal)
    }

    fun clearSharedPrefs() {
        val emptyList = mutableListOf<Meal>()
        saveList(emptyList)
    }
}

