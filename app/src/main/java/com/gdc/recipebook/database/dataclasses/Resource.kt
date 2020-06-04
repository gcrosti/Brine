package com.gdc.recipebook.database.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "resources_table")
data class Resource(
    @PrimaryKey(autoGenerate = true)
    var resourceId: Long = 0L,

    @ColumnInfo(name = "mealId")
    var mealId:Long,

    @ColumnInfo(name = "resourceURL")
    var resourceURL: String = ""
)