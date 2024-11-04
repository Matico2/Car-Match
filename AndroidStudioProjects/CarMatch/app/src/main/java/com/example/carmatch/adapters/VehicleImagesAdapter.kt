package com.example.carmatch.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carmatch.model.AdVehicle
import com.example.carmatch1.databinding.ItemVehicleImageBinding
import com.squareup.picasso.Picasso

class VehicleImagesAdapter(private val vehicles: List<AdVehicle>) :
    RecyclerView.Adapter<VehicleImagesAdapter.VehicleImageViewHolder>() {
    
    inner class VehicleImageViewHolder(private val binding: ItemVehicleImageBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(vehicle: AdVehicle) {
            if (vehicle.imageUrls.isNotEmpty()) {
                Picasso.get().load(vehicle.imageUrls[0]).into(binding.vehicleImageView)
            }
            binding.vehicleModel.text = vehicle.model
            binding.vehiclePrice.text = "Preço: R$ ${vehicle.price}"
            binding.vehicleLocation.text = "Localização: ${vehicle.location}"
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleImageViewHolder {
        val binding = ItemVehicleImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehicleImageViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: VehicleImageViewHolder, position: Int) {
        holder.bind(vehicles[position])
    }
    
    override fun getItemCount(): Int {
        return vehicles.size
    }
}
