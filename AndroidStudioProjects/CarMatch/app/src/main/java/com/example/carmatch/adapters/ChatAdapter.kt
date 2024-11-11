package com.example.carmatch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.carmatch.model.User
import com.example.carmatch1.databinding.ItemChatBinding
import com.squareup.picasso.Picasso

class ChatAdapter: Adapter<ChatAdapter.ChatViewHolder>() {
    private var listChat = emptyList<User>()
    fun addList(list: List<User>){
        listChat = list
        notifyDataSetChanged()
    }
    inner class ChatViewHolder (
        private val binding:ItemChatBinding
    ):ViewHolder(binding.root){
        fun bind(user: User){
            binding.textUserName.text = user.name
            Picasso.get()
                .load(user.photoUser)
                .into(binding.imageContatoFoto)
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
       val inflater= LayoutInflater.from(parent.context)
        val itemView = ItemChatBinding.inflate(
            inflater, parent, false
        )
        return ChatViewHolder(itemView)
    }
    
    override fun getItemCount(): Int {
       return listChat.size
    }
    
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
       val user = listChat[position]
        holder.bind(user)
    }
}