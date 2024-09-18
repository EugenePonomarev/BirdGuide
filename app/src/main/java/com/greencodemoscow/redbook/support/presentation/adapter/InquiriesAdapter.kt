package com.greencodemoscow.redbook.support.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.greencodemoscow.redbook.databinding.RedBookItemBinding
import com.greencodemoscow.redbook.support.data.model.InquiriesData

class InquiriesAdapter(private val onItemClicked: (InquiriesData) -> Unit) :
    ListAdapter<InquiriesData, InquiriesAdapter.ItemViewHolder>(
        InquiriesItemDiffCallback
    ) {

    class ItemViewHolder(private val binding: RedBookItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: InquiriesData, onItemClicked: (InquiriesData) -> Unit) {
            with(binding) {
                itemName.text = item.name
                parkName.text = item.descritption
                root.setOnClickListener { onItemClicked(item) }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val binding = RedBookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClicked)
    }
}

object InquiriesItemDiffCallback: DiffUtil.ItemCallback<InquiriesData>() {
    override fun areItemsTheSame(oldItem: InquiriesData, newItem: InquiriesData): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: InquiriesData, newItem: InquiriesData): Boolean {
        return oldItem == newItem
    }
}