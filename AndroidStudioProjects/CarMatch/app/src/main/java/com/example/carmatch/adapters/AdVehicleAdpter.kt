package com.example.carmatch.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carmatch.model.AdVehicle
import com.example.carmatch.model.User
import com.example.carmatch.model.Vehicle
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ItemVehicleImageBinding
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class AdVehiclesAdapter(
    private var listAd: MutableList<AdVehicle>,
    private val listUser: List<User>,
    private val listVehicle: List<Vehicle>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<AdVehiclesAdapter.AdVehicleViewHolder>() {
    
    interface OnItemClickListener {
        fun onItemClick(adVehicle: AdVehicle)
    }
    
    private val storage by lazy { FirebaseStorage.getInstance() }
    
    inner class AdVehicleViewHolder(private val binding: ItemVehicleImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(adVehicle: AdVehicle) {
            val vehicle = listVehicle.find { it.vehicleId == adVehicle.idVehicle }
            val user = listUser.find { it.idUser == adVehicle.idUser }
            
            if (vehicle != null && user != null) {
                binding.vehicleModel.text = vehicle.model
                binding.vehiclePrice.text = "R$ ${vehicle.price}"
                binding.userName.text = "Anunciante: ${user.name}"
                binding.vehicleLocation.text = "Localização: ${user.city}"
                
                // Carrega a imagem principal do veículo
                val imageRef = storage.reference.child("photos/vehicles/${user.idUser}/${vehicle.vehicleId}/photo_0.jpg")
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    Picasso.get().load(uri).into(binding.vehicleImages)
                }
                
                // Lógica para mostrar ou esconder informações adicionais
                binding.additionalInfoLayout.visibility = View.GONE
                binding.tvViewMore.text = "Ver mais"
                binding.tvViewMore.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0)
                binding.tvViewMore.setOnClickListener {
                    val isVisible = binding.additionalInfoLayout.visibility == View.VISIBLE
                    binding.additionalInfoLayout.visibility = if (isVisible) View.GONE else View.VISIBLE
                    binding.tvViewMore.text = if (isVisible) "Ver mais" else "Ver menos"
                    binding.tvViewMore.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, if (isVisible) R.drawable.arrow_down else R.drawable.arrow_up, 0
                    )
                    if (!isVisible) {
                        binding.vehicleYear.text = "Ano: ${vehicle.year}"
                        binding.vehicleFuelType.text = "Combustível: ${vehicle.fuelType}"
                        binding.vehicleCondition.text = "Condição: ${vehicle.condition}"
                        binding.vehicleKm.text = "KM: ${vehicle.km}"
                        binding.vehicleDescription.text = "Descrição: ${vehicle.description}"
                    }
                }
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdVehicleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVehicleImageBinding.inflate(inflater, parent, false)
        return AdVehicleViewHolder(binding)
    }
    
    override fun getItemCount(): Int = listAd.size
    
    override fun onBindViewHolder(holder: AdVehicleViewHolder, position: Int) {
        holder.bind(listAd[position])
    }
}
