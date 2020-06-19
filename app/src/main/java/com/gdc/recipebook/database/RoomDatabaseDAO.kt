package com.gdc.recipebook.database

import androidx.room.*
import com.gdc.recipebook.database.dataclasses.*


@Dao
interface RoomDatabaseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMeal(meal:Meal): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFunction(mealFunction:MealFunction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertResource(resource: Resource)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(image: Image)

    @Delete
    fun deleteMeal(meal:Meal)

    @Query("DELETE FROM functions_table WHERE functionMealId ==:mealId ")
    fun deleteFunctionsFromId(mealId:Long)

    @Query("DELETE FROM images_table WHERE imageMealId == :mealId")
    fun deleteImagesFromId(mealId:Long)

    @Query("DELETE FROM resources_table WHERE resourceMealId == :mealId")
    fun deleteResourcesFromId(mealId:Long)

    @Transaction
    @Query("SELECT * FROM meals_table")
    fun getAllMealsWithFunctions(): List<MealWithFunctions>

    @Query("SELECT * FROM functions_table")
    fun getAllFunctions(): List<MealFunction>

    @Query("SELECT * FROM meals_table WHERE name == :mealName")
    fun getMealFromName(mealName: String): Meal

}