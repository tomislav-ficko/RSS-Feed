package com.ficko.rssfeed.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ficko.rssfeed.databinding.ListViewHolderBinding
import com.ficko.rssfeed.domain.RssFeedItem

class RssFeedItemAdapter(
    private val items: List<RssFeedItem>
) : RecyclerView.Adapter<RssFeedItemAdapter.ListViewHolder>() {

    inner class ListViewHolder(
        private val binding: ListViewHolderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(feedItem: RssFeedItem) {
            binding.apply {
                imageUrl = feedItem.imageUrl
                titleValue = feedItem.name
                descriptionValue = feedItem.description
                binding.root.setOnClickListener { listener?.itemClicked(feedItem) }
            }
        }
    }

    interface ListViewHolderListener {
        fun itemClicked(item: RssFeedItem)
    }

    private lateinit var binding: ListViewHolderBinding
    private var listener: ListViewHolderListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = ListViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setListener(listener: ListViewHolderListener) {
        this.listener = listener
    }
}