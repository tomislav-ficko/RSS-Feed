package com.ficko.rssfeed.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ficko.rssfeed.databinding.RssFeedViewHolderBinding
import com.ficko.rssfeed.domain.RssFeed

class RssFeedAdapter(
    private val items: List<RssFeed>
) : RecyclerView.Adapter<RssFeedAdapter.RssFeedViewHolder>() {

    inner class RssFeedViewHolder(
        private val binding: RssFeedViewHolderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(rssFeed: RssFeed) {
            binding.item = rssFeed
            binding.root.setOnClickListener { listener?.itemClicked(rssFeed) }
        }
    }

    interface RssFeedViewHolderListener {
        fun itemClicked(item: RssFeed)
    }

    private lateinit var binding: RssFeedViewHolderBinding
    private var listener: RssFeedViewHolderListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RssFeedViewHolder {
        binding = RssFeedViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RssFeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RssFeedViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setListener(listener: RssFeedViewHolderListener) {
        this.listener = listener
    }
}