package com.ficko.rssfeed.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ficko.rssfeed.databinding.ListViewHolderBinding
import com.ficko.rssfeed.domain.CommonRssAttributes
import com.ficko.rssfeed.domain.RssFeedItem

class ListAdapter(
    private val items: List<CommonRssAttributes>
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    inner class ListViewHolder(
        private val binding: ListViewHolderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CommonRssAttributes) {
            binding.apply {
                imageUrl = item.imageUrl
                titleValue = item.name
                descriptionValue = item.description
                binding.viewHolderContainer.setOnClickListener { listener?.itemClicked(item) }
            }
        }
    }

    interface ListViewHolderListener {
        fun itemClicked(item: CommonRssAttributes)
    }

    private lateinit var binding: ListViewHolderBinding
    private var listener: ListViewHolderListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = ListViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        try {
            holder.bind(items[position])
        } catch (e: ClassCastException) {
            convertFeedItemDeserializedAsMapToDomainModel(holder, position)
        }
    }

    override fun getItemCount(): Int = items.size

    fun setListener(listener: ListViewHolderListener) {
        this.listener = listener
    }

    private fun convertFeedItemDeserializedAsMapToDomainModel(holder: ListViewHolder, position: Int) {
        if (items[position] is Map<*, *>) {
            val rssFeedItem = (items[position] as Map<String, String>).convertToRssFeedItem()
            holder.bind(rssFeedItem)
        }
    }

    private fun Map<String, String>.convertToRssFeedItem(): RssFeedItem {
        return RssFeedItem().apply {
            entries.map { attribute ->
                when (attribute.key) {
                    "id" -> id = attribute.value
                    "name" -> name = attribute.value
                    "description" -> description = attribute.value
                    "url" -> url = attribute.value
                    "imageUrl" -> imageUrl = attribute.value
                }
            }
        }
    }
}