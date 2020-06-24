package com.gdc.recipebook.screens.mealeditor.resources

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gdc.recipebook.R
import com.gdc.recipebook.database.dataclasses.Resource
import com.gdc.recipebook.databinding.ViewResourceListItemBinding

class ResourceListItemView(private val binding: ViewResourceListItemBinding):
    RecyclerView.ViewHolder(binding.root){

    fun bind(mealResource: Resource,clickListener: ResourceListListener) {
        binding.resource = mealResource
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ResourceListItemView {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ViewResourceListItemBinding.inflate(layoutInflater,parent,false)
            return ResourceListItemView(binding)
        }
    }
}