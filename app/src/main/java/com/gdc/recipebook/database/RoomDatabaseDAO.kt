package com.gdc.recipebook.database

import androidx.room.*
import com.gdc.recipebook.database.dataclasses.Image
import com.gdc.recipebook.database.dataclasses.MealFunction
import com.gdc.recipebook.database.dataclasses.Meal
import com.gdc.recipebook.database.dataclasses.Resource


@Dao
interface RoomDatabaseDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertMeal(meal:Meal)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertFunction(mealFunction:MealFunction)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertResource(resource: Resource)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertImage(image: Image)

    @Update
    fun updateMeal(meal:Meal)

    @Update
    fun updateFunction(function: MealFunction)

    @Update
    fun updateResource(resource: Resource)

    @Update
    fun updateImage(image: Image)

    @Delete
    fun deleteMeal(meal:Meal)

    @Query("DELETE from meal_functions_table WHERE mealId = :mealId")
    fun deleteMealFunctions(mealId: String)

    @Query("DELETE from resources_table WHERE mealId = :mealId")
    fun deleteMealResources(mealId: String)

    @Query("DELETE from images_table WHERE mealId = :mealId")
    fun deleteMealImages(mealId: String)

}