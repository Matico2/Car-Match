package com.example.carmatch.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.carmatch.model.FavVehicle
import com.example.carmatch.model.Vehicle
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ItemVehicleFavBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class FavVehicleAdapter(
    private val context: Context?,
    private var listFavVehicle: MutableList<FavVehicle>,
    private var listVehicle: MutableList<Vehicle>,
    private val itemClickListener: OnClickListener
) : RecyclerView.Adapter<FavVehicleAdapter.FavVehiclesViewHolder>() {
    
    interface OnClickListener {
        fun onVehicleClick(vehicleId: String)
        fun onRemoveFavoriteClick(vehicleId: String)
        fun onChatClick(chatId: String, userId: String?)
    }
    
    inner class FavVehiclesViewHolder(private val binding: ItemVehicleFavBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(favVehicle: FavVehicle) {
            val vehicle = listVehicle.find { it.vehicleId == favVehicle.idVehicle }
            
            if (vehicle != null) {
                binding.txtModel.text = vehicle.model
                binding.txtBrand.text = vehicle.brand
                binding.txtPrice.text = "R$ ${vehicle.price}"
            } else {
                binding.txtModel.text = "Modelo não encontrado"
                binding.txtBrand.text = "Marca não encontrada"
                binding.txtPrice.text = "--"
            }
            
            val imagePath = "photos/vehicles/${vehicle?.userUID}/${vehicle?.vehicleId}/photo_0.jpg"
            val storageReference = FirebaseStorage.getInstance().reference.child(imagePath)
            
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                Picasso.get()
                    .load(uri)
                    .placeholder(R.drawable.img_vehicle)
                    .into(binding.imgPhoto)
            }.addOnFailureListener {
                Log.e("ImageError", "Erro ao carregar a imagem do veículo: ${it.message}")
            }
            
            binding.btnStartConversation.setOnClickListener {
                itemClickListener.onChatClick(favVehicle.idVehicle, favVehicle.userId)
            }
            
            
            itemView.setOnClickListener {
                itemClickListener.onVehicleClick(favVehicle.idVehicle)
            }
           
            itemView.setOnLongClickListener {
                removeFavorite(favVehicle)
                true
            }
        }
        
       
    }
    private fun removeFavorite(favVehicle: FavVehicle) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("favVehicle")
            .whereEqualTo("idVehicle", favVehicle.idVehicle)
            .get()
            .addOnSuccessListener { snapshot ->
                for (doc in snapshot.documents) {
                    firestore.collection("favVehicle").document(doc.id).delete()
                        .addOnSuccessListener {
                            val position = listFavVehicle.indexOf(favVehicle)
                            if (position != -1) {
                                listFavVehicle.removeAt(position)
                                listVehicle.removeAt(position)
                                notifyItemRemoved(position)
                            }
                            Log.d("FavVehicleAdapter", "Veículo removido dos favoritos: ${favVehicle.idVehicle}")
                            // Exibindo a mensagem ao usuário
                            Toast.makeText(
                                context, // erro aqui
                                "Veículo removido dos favoritos!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { error ->
                            Log.e("FavVehicleAdapter", "Erro ao remover veículo: ${error.message}")
                        }
                }
            }
            .addOnFailureListener { error ->
                Log.e("FavVehicleAdapter", "Erro ao buscar veículo para remoção: ${error.message}")
            }
    }
    
    override fun onBindViewHolder(holder: FavVehiclesViewHolder, position: Int) {
        holder.bind(listFavVehicle[position])
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavVehiclesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVehicleFavBinding.inflate(inflater, parent, false)
        return FavVehiclesViewHolder(binding)
    }
    
    override fun getItemCount(): Int = listFavVehicle.size
    
}
