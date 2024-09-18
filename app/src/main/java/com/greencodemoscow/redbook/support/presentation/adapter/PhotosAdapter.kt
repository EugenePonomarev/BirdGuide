package com.greencodemoscow.redbook.support.presentation.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greencodemoscow.redbook.R
import com.greencodemoscow.redbook.databinding.ItemPhotoBinding

class PhotosAdapter(
    private val photos: List<Uri>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: Uri, position: Int) {
            Glide.with(binding.root)
                .load(uri)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(binding.imageViewPhoto)
            binding.buttonDelete.setOnClickListener {
                onDeleteClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photos[position], position)
    }

    override fun getItemCount(): Int = photos.size
}