package com.example.carmatch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.carmatch.model.Chat
import com.example.carmatch.model.Vehicle
import com.example.carmatch1.databinding.ItemChatBinding

class ChatAdapter(
    private val onChatClick: (Chat, String) -> Unit // Passa Chat e nome do usuário
) : ListAdapter<Triple<Chat, Vehicle?, String>, ChatAdapter.ChatViewHolder>(ChatDiffCallback()) {
    
    inner class ChatViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(chat: Chat, vehicle: Vehicle?, userName: String) {
            binding.textUserName.text = "Usuário: $userName"
            binding.textStatus.text = if (chat.status) "Ativo" else "Inativo"
            binding.textVehicleModel.text = vehicle?.model ?: "Veículo não encontrado"
            
            // Evento de clique, passa Chat e usuário
            binding.root.setOnClickListener { onChatClick(chat, userName) }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChatBinding.inflate(inflater, parent, false)
        return ChatViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val (chat, vehicle, userName) = getItem(position)
        holder.bind(chat, vehicle, userName)
    }
}

class ChatDiffCallback : DiffUtil.ItemCallback<Triple<Chat, Vehicle?, String>>() {
    override fun areItemsTheSame(
        oldItem: Triple<Chat, Vehicle?, String>,
        newItem: Triple<Chat, Vehicle?, String>
    ): Boolean {
        return oldItem.first.idChat == newItem.first.idChat
    }
    
    override fun areContentsTheSame(
        oldItem: Triple<Chat, Vehicle?, String>,
        newItem: Triple<Chat, Vehicle?, String>
    ): Boolean {
        return oldItem == newItem
    }
}
