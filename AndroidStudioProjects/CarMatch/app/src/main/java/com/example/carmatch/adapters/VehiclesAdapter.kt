package com.example.carmatch.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.carmatch.model.AdVehicle
import com.example.carmatch.model.Vehicle
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ItemVehicleBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.UUID

class VehiclesAdapter(private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<VehiclesAdapter.VehiclesViewHolder>() {
    
    interface OnItemClickListener {
        fun onItemClick(vehicle: Vehicle)
    }
    
    private var listVehicle = emptyList<Vehicle>()
    
    fun addList(list: List<Vehicle>) {
        listVehicle = list
        notifyDataSetChanged()
    }
    
    inner class VehiclesViewHolder(private val binding: ItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vehicle: Vehicle) {
            binding.txtBrand.text = vehicle.brand
            binding.txtModel.text = vehicle.model
            binding.txtPrice.text = "R$ ${vehicle.price}"
            
            var isAdActive = false
            
            FirebaseFirestore.getInstance().collection("AdVehicle")
                .whereEqualTo("idVehicle", vehicle.vehicleId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    isAdActive = querySnapshot.documents.isNotEmpty()
                    updateButtonState(isAdActive)
                }
                .addOnFailureListener {
                    Log.e("FirestoreError", "Erro ao verificar anúncio: ${it.message}")
                }
            
            val imagePath = "photos/vehicles/${vehicle.userUID}/${vehicle.vehicleId}/photo_0.jpg"
            val storageReference = FirebaseStorage.getInstance().reference.child(imagePath)
            
       
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                Picasso.get()
                    .load(uri)
                    .placeholder(R.drawable.img_vehicle)
                    .into(binding.imgPhoto)
            }.addOnFailureListener {
                Log.e("ImageError", "Erro ao carregar a imagem do veículo: ${it.message}")
            }
            
            binding.btnAd.setOnClickListener {
                if (isAdActive) {
                    removeAd(vehicle) { success ->
                        if (success) {
                            isAdActive = false
                            updateButtonState(isAdActive)
                            showToast(binding.root.context, "Anúncio removido com sucesso!")
                        } else {
                            showToast(binding.root.context, "Erro ao remover anúncio.")
                        }
                    }
                } else {
                    createAd(vehicle) { success ->
                        if (success) {
                            isAdActive = true
                            updateButtonState(isAdActive)
                            showToast(binding.root.context, "Anúncio criado com sucesso!")
                        } else {
                            showToast(binding.root.context, "Erro ao criar anúncio.")
                        }
                    }
                }
            }
            
            binding.root.setOnClickListener {
                itemClickListener.onItemClick(vehicle)
            }
        }
        
        
        
        
        private fun updateButtonState(isAdActive: Boolean) {
            binding.btnAd.apply {
                if (isAdActive) {
                    text = "Anunciado"
                    backgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.colorVibrantOrange)
                } else {
                    text = "Anunciar"
                    backgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.colorBlueGray)
                }
            }
        }
        
        
        
        private fun createAd(vehicle: Vehicle, callback: (Boolean) -> Unit) {
            val adVehicle = AdVehicle(
                idUser = vehicle.userUID,
                idVehicle = vehicle.vehicleId,
                idAd = UUID.randomUUID().toString(),
                status = true
            )
            FirebaseFirestore.getInstance().collection("AdVehicle")
                .add(adVehicle)
                .addOnSuccessListener { callback(true) }
                .addOnFailureListener { callback(false) }
        }
        
        private fun removeAd(vehicle: Vehicle, callback: (Boolean) -> Unit) {
            FirebaseFirestore.getInstance().collection("AdVehicle")
                .whereEqualTo("idVehicle", vehicle.vehicleId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.documents.forEach { it.reference.delete() }
                    callback(true)
                }
                .addOnFailureListener { callback(false) }
        }
        
        private fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehiclesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVehicleBinding.inflate(inflater, parent, false)
        return VehiclesViewHolder(binding)
    }
    
    override fun getItemCount(): Int {
        return listVehicle.size
    }
    
    override fun onBindViewHolder(holder: VehiclesViewHolder, position: Int) {
        val vehicle = listVehicle[position]
        holder.bind(vehicle)
    }
}
