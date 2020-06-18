package com.gdc.recipebook.database

import androidx.room.*
import com.gdc.recipebook.database.dataclasses.*


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

    @Transaction
    @Query("SELECT * FROM meals_table")
    fun getAllMeals(): List<MealWithRelations>

    @Query("SELECT * FROM functions_table")
    fun getAllFunctions(): List<MealFunction>

}