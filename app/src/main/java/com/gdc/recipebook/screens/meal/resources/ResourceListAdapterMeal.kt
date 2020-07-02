package com.gdc.recipebook.screens.meal.resources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.gdc.recipebook.database.dataclasses.Resource
import com.gdc.recipebook.databinding.ViewResourceListItemMealBinding

class ResourceListAdapterMeal():
    ListAdapter<Resource, ResourceListItemViewMeal>(ResourceListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceListItemViewMeal {
        return ResourceListItemViewMeal(ViewResourceListItemMealBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ResourceListItemViewMeal, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class ResourceListDiffCallback: DiffUtil.ItemCallback<Resource>() {
    override fun areItemsTheSame(oldItem: Resource, newItem: Resource): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Resource, newItem: Resource): Boolean {
        return oldItem.resourceURL == newItem.resourceURL
    }

}