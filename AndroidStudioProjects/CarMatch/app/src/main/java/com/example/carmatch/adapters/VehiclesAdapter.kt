    package com.example.carmatch.adapters
    
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.ViewGroup
    import androidx.recyclerview.widget.RecyclerView.Adapter
    import androidx.recyclerview.widget.RecyclerView.ViewHolder
    import com.example.carmatch.model.Vehicle
    import com.example.carmatch1.databinding.ItemVehicleBinding
    import com.squareup.picasso.Picasso
    
    class VehiclesAdapter(private val itemClickListener: OnItemClickListener) : Adapter<VehiclesAdapter.VehiclesViewHolder>() {
        
        interface OnItemClickListener {
            fun onItemClick(vehicle: Vehicle)
            fun onAdvertiseClick(vehicle: Vehicle)
        }
        
        private var listVehicle = emptyList<Vehicle>()
        
        fun addList(list: List<Vehicle>) {
            listVehicle = list
            notifyDataSetChanged()
        }
        
        inner class VehiclesViewHolder(private val binding: ItemVehicleBinding) : ViewHolder(binding.root) {
            fun bind(vehicle: Vehicle) {
                val userId = vehicle.userUID
                val vehicleId = vehicle.vehicleId
                val imageUrl = "photos/vehicles/$userId/$vehicleId/photo_0.jpg"
                
                Picasso.get()
                    .load(imageUrl)
                    .into(binding.imgPhoto)
                
                binding.txtBrand.text = vehicle.brand
                binding.txtModel.text = vehicle.model
                binding.txtPrice.text = vehicle.price.toString()
                
                // Adiciona log para verificar o click
                binding.btnAd.setOnClickListener {
                    Log.d("VehiclesAdapter", "Botão de anúncio clicado para veículo: ${vehicle.vehicleId}")
                    itemClickListener.onAdvertiseClick(vehicle)
                }
                
                binding.root.setOnClickListener {
                    Log.d("VehiclesAdapter", "Item do veículo clicado: ${vehicle.vehicleId}")
                    itemClickListener.onItemClick(vehicle)
                }
            }
        }
        
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehiclesViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = ItemVehicleBinding.inflate(inflater, parent, false)
            return VehiclesViewHolder(itemView)
        }
        
        override fun getItemCount(): Int {
            return listVehicle.size
        }
        
        override fun onBindViewHolder(holder: VehiclesViewHolder, position: Int) {
            val vehicle = listVehicle[position]
            holder.bind(vehicle)
        }
        
    }
