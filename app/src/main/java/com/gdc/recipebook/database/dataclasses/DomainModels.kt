package com.gdc.recipebook.database.dataclasses

data class MealWithRelations(
    var meal: Meal,
    var images: MutableList<Image>? = null,
    var functions: MealFunction? = null,
    var resources: MutableList<Resource>? = null
)