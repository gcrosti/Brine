package com.gdc.recipebook.screens.mealeditor.utils

import ImagesAdapter
import android.text.Editable
import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gdc.recipebook.database.dataclasses.Image
import com.gdc.recipebook.database.dataclasses.Resource
import com.gdc.recipebook.screens.mealeditor.resources.ResourceListAdapter


@BindingAdapter("nameToEditable")
fun TextView.setMealNameEditable(mealName: String): Editable {
    return Editable.Factory.getInstance().newEditable(mealName)
}

@BindingAdapter("editorListData")
fun bindRecyclerView(recyclerView: RecyclerView, data: MutableList<Resource>?) {
    val adapter = recyclerView.adapter as ResourceListAdapter
    adapter.submitList(data)

}

@BindingAdapter("imagesListData")
fun bindImagesRecyclerView(recyclerView: RecyclerView, data: MutableList<Image>?) {
    val adapter = recyclerView.adapter as ImagesAdapter
    adapter.addHeaderAndSubmitList(data)
    Log.d("image list submitted", data.toString())
}