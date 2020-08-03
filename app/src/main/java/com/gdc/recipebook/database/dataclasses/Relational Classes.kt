package com.gdc.recipebook.database.dataclasses

import androidx.room.Embedded
import androidx.room.Relation

data class MealWithFunctions (
    @Embedded val meal: Meal,
    @Relation(
        parentColumn = "mealId",
        entityColumn = "functionMealId"
    )
    var mealFunctions: MealFunction?
)