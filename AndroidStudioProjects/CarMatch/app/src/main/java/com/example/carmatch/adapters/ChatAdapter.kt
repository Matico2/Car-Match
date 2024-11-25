package com.example.carmatch.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.carmatch.model.Chat
import com.example.carmatch.model.User
import com.example.carmatch.model.Vehicle
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ItemChatBinding
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class ChatAdapter(
    private val onChatClick: (User) -> Unit
) : ListAdapter<Triple<Chat, Vehicle?, String>, ChatAdapter.ChatViewHolder>(DiffCallback) {
    
    inner class ChatViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        private val storage by lazy { FirebaseStorage.getInstance() }
        
        fun bind(chat: Chat, vehicle: Vehicle?, userName: String) {
            binding.textUserName.text = userName
            binding.textVehicleModel.text = vehicle?.model ?: "Veículo não encontrado"
            
            vehicle?.let {
                val imageRef = storage.reference.child("photos/vehicles/${vehicle.vehicleId}/photo_0.jpg")
                imageRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        Picasso.get()
                            .load(uri)
                            .placeholder(R.drawable.img_vehicle)
                            .error(R.drawable.vehicle_icon)
                            .into(binding.imageContatoFoto)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Firebase Storage", "Erro ao carregar imagem: ${exception.message}")
                        binding.imageContatoFoto.setImageResource(R.drawable.vehicle_icon)
                    }
            } ?: run {
                binding.imageContatoFoto.setImageResource(R.drawable.vehicle_icon)
            }
            
            binding.root.setOnClickListener {
                val otherUserId = chat.participants.firstOrNull { it != chat.idUser1 } ?: return@setOnClickListener
                val user = User(
                    idUser = otherUserId,
                    name = userName
                )
                onChatClick(user)
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val (chat, vehicle, userName) = getItem(position)
        holder.bind(chat, vehicle, userName)
    }
    
    companion object DiffCallback : DiffUtil.ItemCallback<Triple<Chat, Vehicle?, String>>() {
        override fun areItemsTheSame(oldItem: Triple<Chat, Vehicle?, String>, newItem: Triple<Chat, Vehicle?, String>) =
            oldItem.first.idChat == newItem.first.idChat
        
        override fun areContentsTheSame(oldItem: Triple<Chat, Vehicle?, String>, newItem: Triple<Chat, Vehicle?, String>) =
            oldItem == newItem
    }
}
