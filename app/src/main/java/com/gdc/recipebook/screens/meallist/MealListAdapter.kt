package com.gdc.recipebook.screens.meallist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.gdc.recipebook.database.dataclasses.Meal

class MealListAdapter():
    ListAdapter<Meal,MealListItemView>(MealListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealListItemView {
        return MealListItemView.from(parent)
    }

    override fun onBindViewHolder(holder: MealListItemView, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}


class MealListDiffCallback: DiffUtil.ItemCallback<Meal>() {
    override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
        return oldItem.mealId == newItem.mealId
    }

    override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
        if (oldItem.name == newItem.name) {
            return oldItem.mealFunctions == newItem.mealFunctions
        }
        return false
    }
}