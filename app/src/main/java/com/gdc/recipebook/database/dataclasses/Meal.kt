package com.gdc.recipebook.database.dataclasses

import androidx.room.*

@Entity(tableName = "meals_table")
data class Meal (
    @PrimaryKey(autoGenerate = true)
    var mealId: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "notes")
    var notes: String = ""
)




