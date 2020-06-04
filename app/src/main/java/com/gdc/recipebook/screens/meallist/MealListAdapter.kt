package com.gdc.recipebook.screens.meallist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gdc.recipebook.database.dataclasses.Meal
import java.util.*

class MealListAdapter(var mealList: List<Meal>, private val clickListener: View.OnClickListener):
    RecyclerView.Adapter<MealListItemView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealListItemView {
        val inflater = LayoutInflater.from(parent.context)
        return MealListItemView(
            inflater,
            parent
        )
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: MealListItemView, position: Int) {
        val sortedList = mealList.sortedBy { it.name.toLowerCase(Locale.ROOT) }
        holder.bind(sortedList[position])
        holder.itemView.setOnClickListener(clickListener)
    }
}