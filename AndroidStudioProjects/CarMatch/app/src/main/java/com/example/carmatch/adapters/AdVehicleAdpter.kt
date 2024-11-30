package com.example.carmatch.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.carmatch.model.AdVehicle
import com.example.carmatch.model.User
import com.example.carmatch.model.Vehicle
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ItemVehicleImageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AdVehiclesAdapter(
    private val listAd: MutableList<AdVehicle>,
    private val listUser: List<User>,
    private val listVehicle: List<Vehicle>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<AdVehiclesAdapter.AdVehicleViewHolder>() {
    
    interface OnItemClickListener {
        fun onItemClick(adVehicle: AdVehicle)
    }
    
    inner class AdVehicleViewHolder(private val binding: ItemVehicleImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(adVehicle: AdVehicle) {
            val vehicle = listVehicle.find { it.vehicleId == adVehicle.idVehicle }
            val user = listUser.find { it.idUser == adVehicle.idUser }
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
            
            if (vehicle != null && user != null) {
                // Configurar informações básicas do veículo e anunciante
                binding.vehicleModel.text = vehicle.model
                binding.vehiclePrice.text = "R$ ${vehicle.price}"
                binding.userName.text = "Anunciante: ${user.name}"
                binding.vehicleLocation.text = "Localização: ${user.city}"
                
                // Configurar informações adicionais do veículo
                binding.vehicleYear.text = "Ano: ${vehicle.year}"
                binding.vehicleFuelType.text = "Combustível: ${vehicle.fuelType}"
                binding.vehicleCondition.text = "Condição: ${vehicle.condition}"
                binding.vehicleKm.text = "KM: ${vehicle.km}"
                binding.vehicleDescription.text = "Descrição: ${vehicle.description}"
            }
            
            // Configurar imagens no ViewPager2
            val storage = FirebaseStorage.getInstance()
            val imageUrls = mutableListOf<String>()
            
            if (vehicle != null && user != null) {
                val storageRef = storage.reference.child("photos/vehicles/${user.idUser}/${vehicle.vehicleId}")
                storageRef.listAll()
                    .addOnSuccessListener { listResult ->
                        listResult.items.forEach { photoRef ->
                            photoRef.downloadUrl.addOnSuccessListener { uri ->
                                imageUrls.add(uri.toString())
                                if (imageUrls.size == listResult.items.size) {
                                    val imagesAdapter = VehicleImagesAdapter(imageUrls)
                                    binding.viewPagerImages.adapter = imagesAdapter
                                    binding.viewPagerImages.apply {
                                        orientation = ViewPager2.ORIENTATION_HORIZONTAL
                                        offscreenPageLimit = 3
                                    }
                                }
                            }
                        }
                    }
            }
            
            val firestore = FirebaseFirestore.getInstance()
            
            // Verificar se o veículo já é favoritado
            firestore.collection("favVehicle")
                .whereEqualTo("idVehicle", adVehicle.idVehicle)
                .whereEqualTo("idAd", adVehicle.idAd)
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.documents.isNotEmpty()) {
                        binding.heartIcon.setColorFilter(binding.root.context.getColor(R.color.red))
                    } else {
                        binding.heartIcon.setColorFilter(binding.root.context.getColor(android.R.color.darker_gray))
                    }
                }
            
            // Configuração do clique no ícone de favoritos
            binding.heartIcon.setOnClickListener {
                firestore.collection("favVehicle")
                    .whereEqualTo("idVehicle", adVehicle.idVehicle)
                    .whereEqualTo("idAd", adVehicle.idAd)
                    .whereEqualTo("userId", currentUserId)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.documents.isNotEmpty()) {
                            // Remover dos favoritos
                            val favId = querySnapshot.documents[0].id
                            firestore.collection("favVehicle").document(favId).delete()
                                .addOnSuccessListener {
                                    binding.heartIcon.setColorFilter(binding.root.context.getColor(android.R.color.darker_gray))
                                    Toast.makeText(
                                        binding.root.context,
                                        "Veículo removido dos favoritos",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            // Adicionar aos favoritos
                            val favVehicle = hashMapOf(
                                "idVehicle" to adVehicle.idVehicle,
                                "idAd" to adVehicle.idAd,
                                "userId" to currentUserId,
                                "status" to true
                            )
                            firestore.collection("favVehicle").add(favVehicle)
                                .addOnSuccessListener {
                                    binding.heartIcon.setColorFilter(binding.root.context.getColor(R.color.red))
                                    Toast.makeText(
                                        binding.root.context,
                                        "Veículo salvo nos favoritos",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
            }
            
            // Configurar o clique no botão "Ver mais"
            binding.tvViewMore.setOnClickListener {
                if (binding.additionalInfoLayout.visibility == View.GONE) {
                    binding.additionalInfoLayout.visibility = View.VISIBLE
                    binding.tvViewMore.text = "Ver menos"
                } else {
                    binding.additionalInfoLayout.visibility = View.GONE
                    binding.tvViewMore.text = "Ver mais"
                }
            }
            
            // Configuração do clique no botão de conversa
            binding.btnStartConversation.setOnClickListener {
                itemClickListener.onItemClick(adVehicle)
            }
        }
    }
    
    
    
    
    
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdVehicleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVehicleImageBinding.inflate(inflater, parent, false)
        return AdVehicleViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: AdVehicleViewHolder, position: Int) {
        holder.bind(listAd[position])
    }
    
    
    override fun getItemCount(): Int = listAd.size
}
