package com.gdc.recipebook.database.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "images_table")
data class Image(
    @PrimaryKey(autoGenerate = true)
    var imageId: Long = 0L,

    @ColumnInfo(name = "MealId")
    var mealId: Long,

    @ColumnInfo(name = "imageURL")
    var imageURL: String = ""
)