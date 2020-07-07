
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gdc.recipebook.database.dataclasses.Image
import com.gdc.recipebook.screens.mealeditor.images.HeaderListener
import com.gdc.recipebook.screens.mealeditor.images.HeaderViewHolder
import com.gdc.recipebook.screens.mealeditor.images.ImageListener
import com.gdc.recipebook.screens.mealeditor.images.ImageViewHolder

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class ImagesAdapter(private val imageClickListener: ImageListener, private val headerClickListener: HeaderListener):
        ListAdapter<DataItem,RecyclerView.ViewHolder>(ImagesDiffCallback()) {

    fun addHeaderAndSubmitList(list: List<Image>?) {
        val items = when (list) {
            null -> listOf(DataItem.header)
            else -> listOf(DataItem.header) + list.map { DataItem.Image(it) }
        }
        submitList(items)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ImageViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> {
                val item = getItem(position) as DataItem.Image
                holder.bind(item = item.image, listener = imageClickListener)
                Log.d("item bound", item.image.toString())
            }
            is HeaderViewHolder -> {
                holder.bind(listener = headerClickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Image -> ITEM_VIEW_TYPE_ITEM
            is DataItem.header -> ITEM_VIEW_TYPE_HEADER
        }
    }
}



class ImagesDiffCallback: DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}


sealed class DataItem {
    data class Image(val image: com.gdc.recipebook.database.dataclasses.Image): DataItem()  {
        override val id = image.imageId
    }

    object header: DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}