package com.gdc.recipebook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MealListAdapter(var mealList: List<Meal>, private val clickListener: View.OnClickListener):
    RecyclerView.Adapter<ListItemView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemView {
        val inflater = LayoutInflater.from(parent.context)
        return ListItemView(inflater,parent)
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: ListItemView, position: Int) {
        val sortedList = mealList.sortedBy { it.name.toLowerCase(Locale.ROOT) }
        holder.bind(sortedList[position])
        holder.itemView.setOnClickListener(clickListener)
    }

    fun updateList(newList: List<Meal>) {
        mealList = newList
        notifyDataSetChanged()
    }

}