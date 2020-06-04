package com.gdc.recipebook.screens.mealeditor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gdc.recipebook.R

class ResourceListItemView(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.view_resource_list_item,parent,false)){
    var urlView = itemView.findViewById<TextView>(R.id.resourceURL)
    var deleteResourceButton = itemView.findViewById<Button>(R.id.deleteResource)

    fun bind(url: String,clickListener: View.OnClickListener) {
        urlView.text = url
        deleteResourceButton.setOnClickListener(clickListener)
    }
}