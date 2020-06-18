package com.gdc.recipebook.screens.meallist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gdc.recipebook.database.dataclasses.Meal
import com.gdc.recipebook.R
import com.gdc.recipebook.database.dataclasses.MealWithRelations
import com.gdc.recipebook.databinding.ViewMealListItemBinding

class MealListItemView(private val binding: ViewMealListItemBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(meal: MealWithRelations) {
        binding.meal = meal.mealWithResources.meal
        Log.d("meal functions", meal.mealWithResources.meal.mealFunctions.toString())
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