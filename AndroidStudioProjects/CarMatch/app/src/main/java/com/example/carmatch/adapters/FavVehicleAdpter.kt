package com.example.carmatch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.carmatch.model.Vehicle
import com.example.carmatch1.databinding.ItemVehicleBinding

class FavVehicleAdapter(private val listener: OnClickListener) :
    ListAdapter<Vehicle, FavVehicleAdapter.FavVehiclesViewHolder>(DiffCallback()) {
    
    interface OnClickListener {
        fun onVehicleClick(vehicleId: String)
    }
    
    inner class FavVehiclesViewHolder(private val binding: ItemVehicleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(vehicle: Vehicle) {
            binding.txtModel.text = vehicle.model
            binding.txtPrice.text = vehicle.price.toString()
            // Configure mais informações do veículo aqui
            
            binding.root.setOnClickListener {
                listener.onVehicleClick(vehicle.vehicleId)
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavVehiclesViewHolder {
        val binding = ItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavVehiclesViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: FavVehiclesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class DiffCallback : DiffUtil.ItemCallback<Vehicle>() {
        override fun areItemsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
            return oldItem.vehicleId == newItem.vehicleId
        }
        
        override fun areContentsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
            return oldItem == newItem
        }
    }
}
