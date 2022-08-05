package com.ficko.rssfeed.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ficko.rssfeed.databinding.ListViewHolderBinding
import com.ficko.rssfeed.domain.CommonRssAttributes

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
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setListener(listener: ListViewHolderListener) {
        this.listener = listener
    }
}