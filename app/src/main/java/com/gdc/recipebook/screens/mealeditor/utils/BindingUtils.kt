package com.gdc.recipebook.screens.mealeditor.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.gdc.recipebook.database.dataclasses.Meal

@BindingAdapter("nameToEditable")
fun TextView.setMealNameEditable(mealName: String): Editable {
    return Editable.Factory.getInstance().newEditable(mealName)
}