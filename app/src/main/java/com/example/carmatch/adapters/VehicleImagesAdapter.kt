package com.example.carmatch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carmatch1.databinding.ItemImageBinding
import com.squareup.picasso.Picasso

class VehicleImagesAdapter(private val imageUrls: List<String>) :
    RecyclerView.Adapter<VehicleImagesAdapter.ImageViewHolder>() {
    
    inner class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            Picasso.get().load(imageUrl).into(binding.imageView)
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemImageBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageUrls[position])
    }
    
    override fun getItemCount(): Int = imageUrls.size
}
