package com.gdc.recipebook.screens.meal.resources

import androidx.recyclerview.widget.RecyclerView
import com.gdc.recipebook.database.dataclasses.Resource
import com.gdc.recipebook.databinding.ViewResourceListItemMealBinding

class ResourceListItemViewMeal(private val binding: ViewResourceListItemMealBinding):

    RecyclerView.ViewHolder(binding.root){
    fun bind(mealResource: Resource) {
        binding.resource = mealResource
        binding.executePendingBindings()
    }
}