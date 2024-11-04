package com.example.carmatch.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carmatch.model.Vehicle
import com.example.carmatch1.R
import com.squareup.picasso.Picasso

class ViewPagerAdapterFav(private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<ViewPagerAdapterFav.FavVehiclesViewHolder>() {
    
    interface OnItemClickListener {
        fun onItemClick(vehicle: Vehicle)
        fun onHeartClick(vehicle: Vehicle, heartIcon: ImageButton)
        fun onChatClick(vehicle: Vehicle)
    }
    
    private var listVehicle = emptyList<Vehicle>()
    
    fun addList(list: List<Vehicle>) {
        listVehicle= list
        notifyDataSetChanged()
    }
    
    inner class FavVehiclesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.imgPhoto)
        val txtModel: TextView = itemView.findViewById(R.id.txtModel)
        val txtBrand: TextView = itemView.findViewById(R.id.txtBrand)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
        val heartIcon: ImageButton = itemView.findViewById(R.id.heartIcon)
        val btnStartConversation: ImageButton = itemView.findViewById(R.id.btnStartConversation)
        
        fun bind(vehicle: Vehicle) {
            val userId = vehicle.userUID
            val vehicleId = vehicle.vehicleId
            val imageUrl = "photos/vehicles/$userId/$vehicleId/photo_0.jpg"
            
            Picasso.get()
                .load(imageUrl)
                .into(imgPhoto)
            
            txtModel.text = vehicle.model
            txtBrand.text = vehicle.brand
            txtPrice.text = vehicle.price.toString()
            
            heartIcon.setOnClickListener {
                itemClickListener.onHeartClick(vehicle, heartIcon)
            }
            
            btnStartConversation.setOnClickListener {
                itemClickListener.onChatClick(vehicle)
            }
            
            itemView.setOnClickListener {
                itemClickListener.onItemClick(vehicle)
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavVehiclesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle_fav, parent, false)
        return FavVehiclesViewHolder(itemView)
    }
    
    override fun getItemCount(): Int {
        return listVehicle.size
    }
    
    override fun onBindViewHolder(holder: FavVehiclesViewHolder, position: Int) {
        val vehicle = listVehicle[position]
        holder.bind(vehicle)
    }
}