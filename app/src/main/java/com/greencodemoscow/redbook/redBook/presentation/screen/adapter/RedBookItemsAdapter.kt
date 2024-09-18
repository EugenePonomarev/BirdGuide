package com.greencodemoscow.redbook.redBook.presentation.screen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.greencodemoscow.redbook.databinding.RedBookItemBinding
import com.greencodemoscow.redbook.redBook.data.model.RedBookItem

class RedBookItemsAdapter(
    private val onItemClicked: (RedBookItem) -> Unit
) : ListAdapter<RedBookItem, RedBookItemsAdapter.ItemViewHolder>(RedBookItemDiffCallback) {

    class ItemViewHolder(private val binding: RedBookItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RedBookItem, onItemClicked: (RedBookItem) -> Unit) {
            with(binding) {
                itemName.text = item.name
                parkName.text = item.habitatArea

                root.setOnClickListener {
                    onItemClicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RedBookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClicked)
    }
}

// DiffUtil implementation to optimize list updating
object RedBookItemDiffCallback : DiffUtil.ItemCallback<RedBookItem>() {
    override fun areItemsTheSame(oldItem: RedBookItem, newItem: RedBookItem): Boolean {
        // The name is assumed to be a unique identifier.
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: RedBookItem, newItem: RedBookItem): Boolean {
        // Checking for equality of all fields
        return oldItem == newItem
    }
}