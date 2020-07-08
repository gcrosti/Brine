package com.gdc.recipebook.screens.mealeditor.images

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdc.recipebook.database.dataclasses.Image
import com.gdc.recipebook.databinding.ViewImageListHeaderBinding
import com.gdc.recipebook.databinding.ViewImageListItemBinding
import kotlinx.android.synthetic.main.fragment_meal.*

class ImageViewHolder private constructor(val binding: ViewImageListItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Image, listener: ImageListener) {
        binding.image = item
        binding.listener = listener
        binding.executePendingBindings()

        Glide
            .with(binding.root)
            .load(item.imageURL)
            .centerCrop()
            .into(binding.mealImage)
    }

    companion object {
        fun from(parent: ViewGroup): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ViewImageListItemBinding.inflate(layoutInflater,parent,false)
            return ImageViewHolder(binding)
        }
    }
}

class HeaderViewHolder private constructor(val binding: ViewImageListHeaderBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(listener: HeaderListener) {
        binding.clicklistener = listener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ViewImageListHeaderBinding.inflate(layoutInflater,parent,false)


            return HeaderViewHolder(binding)
        }

    }
}


class ImageListener(val clickListener: (image: Image) -> Unit) {
    fun onClick(image: Image) = clickListener(image)
}

class HeaderListener(val clickListener: () -> Unit) {
    fun onClick() = clickListener()
}

