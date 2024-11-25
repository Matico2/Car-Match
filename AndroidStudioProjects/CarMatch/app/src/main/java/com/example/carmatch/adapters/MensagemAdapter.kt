package com.example.carmatch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carmatch.model.Menssage
import com.example.carmatch.utils.Constants
import com.example.carmatch1.databinding.ItemMensafensDestBinding
import com.example.carmatch1.databinding.ItemMensagemSendBinding
import com.google.firebase.auth.FirebaseAuth

class MensagemAdapter(
    private val onMessageLongClick: (Menssage) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    private var listMensagem = emptyList<Menssage>()
    
    fun addList(list: List<Menssage>) {
        listMensagem = list // Atualiza a lista interna
        notifyDataSetChanged() // Notifica a RecyclerView sobre as mudanças
    }
    
    
    // Método para recuperar mensagens atuais
    fun getCurrentMessages(): List<Menssage> {
        return listMensagem
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constants.TYPE_SEND) {
            MensagensSendViewHolder.inflateLayout(parent)
        } else {
            MensagensDestViewHolder.inflateLayout(parent)
        }
    }
    
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mensagem = listMensagem[position]
        when (holder) {
            is MensagensSendViewHolder -> {
                holder.bind(mensagem)
                holder.itemView.setOnLongClickListener {
                    onMessageLongClick(mensagem)
                    true
                }
            }
            is MensagensDestViewHolder -> {
                holder.bind(mensagem)
                holder.itemView.setOnLongClickListener {
                    onMessageLongClick(mensagem)
                    true
                }
            }
        }
    }
    
    override fun getItemCount(): Int = listMensagem.size
    
    override fun getItemViewType(position: Int): Int {
        val mensagem = listMensagem[position]
        val idUser = FirebaseAuth.getInstance().currentUser?.uid
        return if (idUser == mensagem.senderId) Constants.TYPE_SEND else Constants.TYPE_DEST
    }
    
    
    class MensagensSendViewHolder(
        private val binding: ItemMensagemSendBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menssage: Menssage) {
            binding.textSend.text = menssage.text
        }
        
        companion object {
            fun inflateLayout(parent: ViewGroup): MensagensSendViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val itemView = ItemMensagemSendBinding.inflate(inflater, parent, false)
                return MensagensSendViewHolder(itemView)
            }
        }
    }
    
    class MensagensDestViewHolder(
        private val binding: ItemMensafensDestBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menssage: Menssage) {
            binding.textDest.text = menssage.text
        }
        
        companion object {
            fun inflateLayout(parent: ViewGroup): MensagensDestViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val itemView = ItemMensafensDestBinding.inflate(inflater, parent, false)
                return MensagensDestViewHolder(itemView)
            }
        }
    }
}