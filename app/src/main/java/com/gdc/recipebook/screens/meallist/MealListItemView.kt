package com.gdc.recipebook.screens.meallist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gdc.recipebook.database.dataclasses.MealWithFunctions
import com.gdc.recipebook.databinding.ViewMealListItemBinding

class MealListItemView(private val binding: ViewMealListItemBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        mealWithFunctions: MealWithFunctions,
        clickListener: MealListListener
    ) {
        binding.clickListener = clickListener
        binding.mealWithFunctions = mealWithFunctions
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): MealListItemView {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ViewMealListItemBinding.inflate(layoutInflater,parent,false)
            return MealListItemView(binding)
        }
    }
}
