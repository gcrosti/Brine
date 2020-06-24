package com.gdc.recipebook.screens.mealeditor.resources

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.gdc.recipebook.database.dataclasses.Resource

class ResourceListAdapter(val clickListener: ResourceListListener):
    ListAdapter<Resource,ResourceListItemView>(ResourceListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceListItemView {
        return ResourceListItemView.from(parent)
    }

    override fun onBindViewHolder(holder: ResourceListItemView, position: Int) {
        val item = getItem(position)
        holder.bind(item,clickListener)
    }
}

class ResourceListDiffCallback: DiffUtil.ItemCallback<Resource>() {
    override fun areItemsTheSame(oldItem: Resource, newItem: Resource): Boolean {
        return oldItem.resourceURL == newItem.resourceURL
    }

    override fun areContentsTheSame(oldItem: Resource, newItem: Resource): Boolean {
        return oldItem.resourceURL == newItem.resourceURL
    }

}

class ResourceListListener(val clickListener: (mealResource: Resource) -> Unit) {
    fun onClick(mealResource: Resource) = clickListener(mealResource)
}