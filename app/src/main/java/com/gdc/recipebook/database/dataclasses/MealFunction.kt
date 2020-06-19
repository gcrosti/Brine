package com.gdc.recipebook.database.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "functions_table")
data class MealFunction(
    @PrimaryKey(autoGenerate = true)
    var functionId: Long = 0L,

    @ColumnInfo(name = "functionMealId")
    var functionMealId: Long,

    @ColumnInfo(name = "Protein")
    var protein: Boolean = false,

    @ColumnInfo(name = "veg")
    var veg: Boolean = false,

    @ColumnInfo(name = "starch")
    var starch: Boolean = false,

    @ColumnInfo(name = "ingredient")
    var ingredient: Boolean = false,

    @ColumnInfo(name = "dip")
    var dip: Boolean = false,

    @ColumnInfo(name = "dressing")
    var dressing: Boolean = false,

    @ColumnInfo(name = "beverage")
    var beverage: Boolean = false,

    @ColumnInfo(name = "dessert")
    var dessert: Boolean = false
    )