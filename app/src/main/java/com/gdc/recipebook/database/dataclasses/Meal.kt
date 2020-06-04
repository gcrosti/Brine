package com.gdc.recipebook.database.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.google.firebase.iid.FirebaseInstanceId

@Entity(tableName = "meals_table")
data class Meal (
    @PrimaryKey(autoGenerate = true)
    var mealId: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "instanceId")
    val instanceId: String = FirebaseInstanceId.getInstance().id,

    @ColumnInfo(name = "notes")
    var notes: String = "",

    @ColumnInfo(name="resources")
    @Relation(
        parentColumn = "mealId",
        entityColumn = "mealId"
    )
    val resources: List<Resource> = listOf(),

    @ColumnInfo(name = "images")
    @Relation(
        parentColumn = "mealId",
        entityColumn = "mealId"
    )
    val images: List<Image> = listOf(),

    @ColumnInfo(name = "functions")
    @Relation(
        parentColumn = "mealId",
        entityColumn = "mealId")
    val mealFunctions: List<MealFunction> = listOf()

)