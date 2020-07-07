package com.gdc.recipebook.database

import androidx.room.*
import com.gdc.recipebook.database.dataclasses.*


@Dao
interface RoomDatabaseDAO {

    //INSERT

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMeal(meal:Meal): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFunction(mealFunction:MealFunction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertResource(resource: Resource)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(image: Image)


    // DELETE

    @Delete
    fun deleteMeal(meal:Meal)

    @Delete
    fun deleteFunctions(function: MealFunction)

    @Delete
    fun deleteImage(image:Image)

    @Query("DELETE FROM images_table WHERE imageURL == :url")
    fun deleteImageFromUrl(url: String)

    @Delete
    fun deleteResource(resource: Resource)

    @Query("DELETE FROM resources_table WHERE resourceURL ==:url")
    fun deleteFromResourcesWithUrl(url: String)

    @Query("DELETE FROM functions_table WHERE functionMealId ==:mealId ")
    fun deleteFunctionsFromId(mealId:Long)

    @Query("DELETE FROM images_table WHERE imageMealId == :mealId")
    fun deleteImagesFromId(mealId:Long)

    @Query("DELETE FROM resources_table WHERE resourceMealId == :mealId")
    fun deleteResourcesFromId(mealId:Long)


    //GETTERS

    @Transaction
    @Query("SELECT * FROM meals_table")
    fun getAllMealsWithFunctions(): List<MealWithFunctions>

    @Query("SELECT * FROM functions_table")
    fun getAllFunctions(): List<MealFunction>

    @Query("SELECT * FROM meals_table WHERE name == :mealName")
    fun getMealFromName(mealName: String): Meal

    @Query("SELECT * FROM functions_table WHERE functionMealId == :id")
    fun getFunctionsFromId(id: Long): MealFunction

    @Query("SELECT * FROM images_table WHERE imageMealId == :id")
    fun getImagesFromId(id: Long): List<Image>

    @Query("SELECT * FROM resources_table WHERE resourceMealId == :id")
    fun getResourcesFromId(id:Long): List<Resource>


}