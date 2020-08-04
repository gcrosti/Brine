package com.gdc.recipebook.screens.meal.images

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.gdc.recipebook.R
import com.gdc.recipebook.database.dataclasses.Image
import kotlinx.android.synthetic.main.view_pager_image_item.view.*

class ImageSliderAdapter(private val context: Context, val images: List<Image>?): PagerAdapter() {


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        if (images != null) {
            return images.size
        }
        return 1
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.view_pager_image_item,null)
        view.context?.let {
            Glide
                .with(it)
                .load(images?.get(position)?.imageURL)
                .fallback(ColorDrawable(getColor(view.context,R.color.primaryDarkColor)))
                .centerCrop()
                .into(view.sliderImageView)
        }
        if (images != null) {
            view.sliderText.text = formatText(images.size,position)
        }
        val vp = container as ViewPager
        vp.addView(view,0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)

    }

    private fun formatText(size: Int, position: Int): String {
        return "${position + 1} / $size"
    }
}