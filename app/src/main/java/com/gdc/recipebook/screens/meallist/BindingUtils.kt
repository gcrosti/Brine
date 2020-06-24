package com.gdc.recipebook.screens.meallist

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.gdc.recipebook.database.dataclasses.Meal
import com.gdc.recipebook.database.dataclasses.MealFunction
import com.gdc.recipebook.database.dataclasses.MealWithFunctions

@BindingAdapter("mealFunctionsFormatted")
fun TextView.setMealFunctionsFormatted(functions: MealFunction?) {
    text = functions?.let { convertFuncsToNameList(it) }?.let {
        convertListToString(
            it
    )
    }
}