package com.gdc.recipebook.screens.meallist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gdc.recipebook.database.dataclasses.Meal
import com.gdc.recipebook.R
import com.gdc.recipebook.databinding.ViewMealListItemBinding

class MealListItemView(private val binding: ViewMealListItemBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(meal: Meal) {
        binding.meal = meal
        binding.mealListItemViewModel = MealListItemViewModel()
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




class MealListItemViewModel: ViewModel() {

    private fun navToRecipe(view: View?, name:String) {
        val action =
            MealListFragmentDirections.actionRecipeListFragmentToRecipeFragment()
        action.mealName = name
        view?.findNavController()?.navigate(action)
    }

    fun onMealClick(v: View, mealName: String) {
        navToRecipe(v,mealName)
    }
}