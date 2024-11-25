package com.example.carmatch.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carmatch.model.AdVehicle
import com.example.carmatch.model.User
import com.example.carmatch.model.Vehicle
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ItemVehicleImageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    
    inner class AdVehicleViewHolder(private val binding: ItemVehicleImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val firestore = FirebaseFirestore.getInstance()
        private val auth = FirebaseAuth.getInstance()
        
        fun bind(adVehicle: AdVehicle) {
            val currentUserId = auth.currentUser?.uid ?: return
            val vehicleId = adVehicle.idVehicle
            
            val vehicle = listVehicle.find { it.vehicleId == adVehicle.idVehicle }
            val user = listUser.find { it.idUser == adVehicle.idUser }
            
            if (vehicle != null && user != null) {
                binding.vehicleModel.text = vehicle.model
                binding.vehiclePrice.text = "R$ ${vehicle.price}"
                binding.userName.text = "Anunciante: ${user.name}"
                binding.vehicleLocation.text = "Localização: ${user.city}"
                
                // Verificar se o veículo já está nos favoritos
                checkFavoriteStatus(currentUserId, vehicleId) { isFavorite ->
                    updateHeartIcon(isFavorite)
                    
                    binding.heartIcon.setOnClickListener {
                        if (isFavorite) {
                            removeFavorite(currentUserId, vehicleId) { success ->
                                if (success) {
                                    updateHeartIcon(false)
                                }
                            }
                        } else {
                            addFavorite(currentUserId, adVehicle) { success ->
                                if (success) {
                                    updateHeartIcon(true)
                                }
                            }
                        }
                    }
                }
                
                // Configurar clique no botão de conversa
                binding.btnStartConversation.setOnClickListener {
                    itemClickListener.onItemClick(adVehicle)
                }
                
                // Carregar imagem do veículo
                val imageRef =
                    storage.reference.child("photos/vehicles/${user.idUser}/${vehicle.vehicleId}/photo_0.jpg")
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    Picasso.get().load(uri).into(binding.vehicleImages)
                }
            }
        }
        
        private fun checkFavoriteStatus(userId: String, vehicleId: String, callback: (Boolean) -> Unit) {
            firestore.collection("FavVehicle")
                .whereEqualTo("userId", userId)
                .whereEqualTo("idVehicle", vehicleId)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        Log.d("FavVehicle", "Veículo encontrado nos favoritos: ${documents.documents}")
                        callback(true)
                    } else {
                        Log.d("FavVehicle", "Veículo não encontrado nos favoritos.")
                        callback(false)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("FavVehicle", "Erro ao verificar favoritos: ${e.message}", e)
                    callback(false)
                }
        }
        
        
        private fun addFavorite(userId: String, adVehicle: AdVehicle, callback: (Boolean) -> Unit) {
            val favVehicle = hashMapOf(
                "userId" to userId,
                "idVehicle" to adVehicle.idVehicle,
                "idAd" to adVehicle.idAd,
                "status" to true
            )
            
            firestore.collection("FavVehicle")
                .add(favVehicle)
                .addOnSuccessListener { documentReference ->
                    firestore.collection("FavVehicle").document(documentReference.id)
                        .update("idFav", documentReference.id)
                        .addOnSuccessListener {
                            callback(true)
                        }
                        .addOnFailureListener { e ->
                            Log.e("FavVehicle", "Erro ao salvar favorito: ${e.message}", e)
                            callback(false)
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("FavVehicle", "Erro ao adicionar favorito: ${e.message}", e)
                    callback(false)
                }
        }
        
        private fun removeFavorite(userId: String, vehicleId: String, callback: (Boolean) -> Unit) {
            firestore.collection("FavVehicle")
                .whereEqualTo("userId", userId)
                .whereEqualTo("idVehicle", vehicleId)
                .get()
                .addOnSuccessListener { documents ->
                    val batch = firestore.batch()
                    for (document in documents) {
                        batch.delete(document.reference)
                    }
                    batch.commit()
                        .addOnSuccessListener { callback(true) }
                        .addOnFailureListener { e ->
                            Log.e("FavVehicle", "Erro ao remover favorito: ${e.message}", e)
                            callback(false)
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("FavVehicle", "Erro ao buscar favorito para remoção: ${e.message}", e)
                    callback(false)
                }
        }
        
        private fun updateHeartIcon(isFavorite: Boolean) {
            val color = if (isFavorite) R.color.red else R.color.black
            binding.heartIcon.setColorFilter(binding.root.context.getColor(color))
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
