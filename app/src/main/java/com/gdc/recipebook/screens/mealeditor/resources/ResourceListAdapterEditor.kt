package com.gdc.recipebook.screens.mealeditor.resources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.gdc.recipebook.database.dataclasses.Resource
import com.gdc.recipebook.databinding.ViewResourceListItemBinding

class ResourceListAdapter(val clickListener: ResourceListListener):
    ListAdapter<Resource,ResourceListItemViewEditor>(ResourceListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceListItemViewEditor {
        return ResourceListItemViewEditor(ViewResourceListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ResourceListItemViewEditor, position: Int) {
        val item = getItem(position)
        holder.bind(item,clickListener)
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

class ResourceListListener(val clickListener: (mealResource: Resource) -> Unit) {
    fun onClick(mealResource: Resource) = clickListener(mealResource)
}