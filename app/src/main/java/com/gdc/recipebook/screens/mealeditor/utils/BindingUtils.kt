package com.gdc.recipebook.screens.mealeditor.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gdc.recipebook.database.dataclasses.Meal
import com.gdc.recipebook.database.dataclasses.Resource
import com.gdc.recipebook.screens.meal.resources.ResourceListAdapterMeal
import com.gdc.recipebook.screens.mealeditor.resources.ResourceListAdapter

@BindingAdapter("nameToEditable")
fun TextView.setMealNameEditable(mealName: String): Editable {
    return Editable.Factory.getInstance().newEditable(mealName)
}

@BindingAdapter("editorListData")
fun bindRecyclerView(recyclerView: RecyclerView, data: MutableList<Resource>?) {
    val adapter = recyclerView.adapter as ResourceListAdapter
    adapter.submitList(data)
    Log.d("list submitted", data.toString())
}

