package com.gdc.recipebook.database.dataclasses

import com.gdc.recipebook.database.ImagesFromEditor
import com.gdc.recipebook.database.ResourcesFromEditor

data class MealWithRelations(
    var meal: Meal,
    var images: MutableList<Image>? = null,
    var functions: MealFunction? = null,
    var resources: MutableList<Resource>? = null
)

data class DataForRepo(
    var dish: Meal,
    var images: ImagesFromEditor,
    var functions: MealFunction?,
    var resources: ResourcesFromEditor
)