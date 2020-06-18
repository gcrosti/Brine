package com.gdc.recipebook.screens.meallist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.gdc.recipebook.database.dataclasses.MealWithRelations

class MealListAdapter():
    ListAdapter<MealWithRelations,MealListItemView>(MealListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealListItemView {
        return MealListItemView.from(parent)
    }

    override fun onBindViewHolder(holder: MealListItemView, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}


class MealListDiffCallback: DiffUtil.ItemCallback<MealWithRelations>() {
    override fun areItemsTheSame(oldItem: MealWithRelations, newItem: MealWithRelations): Boolean {
        return oldItem.mealWithResources.mealWithFunctions.meal.mealId == newItem.mealWithResources.mealWithFunctions.meal.mealId
    }

    override fun areContentsTheSame(oldItem: MealWithRelations, newItem: MealWithRelations): Boolean {
        if (oldItem.mealWithResources.mealWithFunctions.meal.name == newItem.mealWithResources.mealWithFunctions.meal.name) {
            return oldItem.mealWithResources.resources == newItem.mealWithResources.resources
        }
        return false
    }
}