package com.gdc.recipebook.screens.meal.resources

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gdc.recipebook.database.dataclasses.Resource

@BindingAdapter("mealListData")
fun bindMealRecyclerView(recyclerView: RecyclerView, data: MutableList<Resource>?) {
    val adapter = recyclerView.adapter as ResourceListAdapterMeal
    adapter.submitList(data)
}