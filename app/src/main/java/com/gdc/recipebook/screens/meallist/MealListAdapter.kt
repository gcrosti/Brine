package com.gdc.recipebook.screens.meallist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.gdc.recipebook.database.dataclasses.MealWithFunctions

class MealListAdapter():
    ListAdapter<MealWithFunctions,MealListItemView>(MealListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealListItemView {
        return MealListItemView.from(parent)
    }

    override fun onBindViewHolder(holder: MealListItemView, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}


class MealListDiffCallback: DiffUtil.ItemCallback<MealWithFunctions>() {
    override fun areItemsTheSame(oldItem: MealWithFunctions, newItem: MealWithFunctions): Boolean {
        return oldItem.meal.mealId == newItem.meal.mealId
    }

    override fun areContentsTheSame(oldItem: MealWithFunctions, newItem: MealWithFunctions): Boolean {
        if (oldItem.meal.name == newItem.meal.name) {
            return oldItem.mealFunctions == newItem.mealFunctions
        }
        return false
    }
}