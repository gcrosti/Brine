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




