package com.gdc.recipebook.screens.meallist

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.gdc.recipebook.database.dataclasses.Meal

@BindingAdapter("mealFunctionsFormatted")
fun TextView.setMealFunctionsFormatted(meal: Meal) {
    text = convertListToString(convertFuncsToNameList(meal.mealFunctions))
}