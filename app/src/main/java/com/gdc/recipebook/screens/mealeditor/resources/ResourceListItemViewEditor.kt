package com.gdc.recipebook.screens.mealeditor.resources

import androidx.recyclerview.widget.RecyclerView
import com.gdc.recipebook.database.dataclasses.Resource
import com.gdc.recipebook.databinding.ViewResourceListItemBinding

class ResourceListItemViewEditor(private val binding: ViewResourceListItemBinding):
    RecyclerView.ViewHolder(binding.root){

    fun bind(mealResource: Resource,clickListener: ResourceListListener) {
        binding.resource = mealResource
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }
}