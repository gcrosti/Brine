package com.gdc.recipebook.screens.mealeditor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gdc.recipebook.screens.mealeditor.ResourceListItemView

class ResourceListAdapter(var resourceList: List<String>,val clickListener: View.OnClickListener):
    RecyclerView.Adapter<ResourceListItemView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceListItemView {
        val inflater = LayoutInflater.from(parent.context)
        return ResourceListItemView(
            inflater,
            parent
        )
    }

    override fun getItemCount(): Int {
        return resourceList.size
    }

    override fun onBindViewHolder(holder: ResourceListItemView, position: Int) {
        holder.bind(resourceList[position],clickListener)
    }
}