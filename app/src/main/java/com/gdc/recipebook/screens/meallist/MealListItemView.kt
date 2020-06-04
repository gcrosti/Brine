package com.gdc.recipebook.screens.meallist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gdc.recipebook.database.dataclasses.Meal
import com.gdc.recipebook.R

class MealListItemView(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.view_meal_list_item,parent,false)) {

    var recipeName = itemView.findViewById<TextView>(R.id.recipeName)
    var recipeFunction = itemView.findViewById<TextView>(R.id.recipeFunction)

    fun bind(meal: Meal) {
        recipeName.text = meal.name
        if (meal.function.isNotEmpty()) {
            recipeFunction.text = meal.function.substring(1,meal.function.length-1)
        }
    }
}