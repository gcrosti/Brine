package com.gdc.recipebook.database.dataclasses

import androidx.room.*
import com.google.firebase.iid.FirebaseInstanceId

@Entity(tableName = "meals_table")
data class Meal (
    @PrimaryKey(autoGenerate = true)
    val mealId: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "instanceId")
    val instanceId: String = FirebaseInstanceId.getInstance().id,

    @ColumnInfo(name = "notes")
    var notes: String = ""
)

data class MealWithFunctions (
    @Embedded val meal: Meal,
    @Relation(
        parentColumn = "mealId",
        entityColumn = "functionMealId"
    )
    var mealFunctions: MealFunction
)


data class MealWithResources(
    @Embedded val mealWithFunctions: MealWithFunctions,
    @Relation(
        parentColumn = "mealId",
        entityColumn = "resourceMealId"
    )
    val resources: MutableList<Resource> = mutableListOf()
)

data class MealWithRelations(
    @Embedded val mealWithResources: MealWithResources,
    @Relation(
        parentColumn = "mealId",
        entityColumn = "imageMealId")
    val images: MutableList<Image> = mutableListOf()
    )
