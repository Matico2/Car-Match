package com.example.carmatch.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carmatch.adapters.ChatAdapter
import com.example.carmatch.model.Chat
import com.example.carmatch.model.Vehicle
import com.example.carmatch.view.activitys.MessageActivity
import com.example.carmatch1.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var chatAdapter: ChatAdapter
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        chatAdapter = ChatAdapter { user ->
            val currentUserId = firebaseAuth.currentUser?.uid ?: return@ChatAdapter
            
            firestore.collection("Chat")
                .whereArrayContains("participants", currentUserId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val chat = querySnapshot.documents.firstOrNull { document ->
                        val participants = document.get("participants") as? List<*>
                        participants?.contains(user.idUser) == true
                    }
                    
                    if (chat != null) {
                        // Chat encontrado, abrir a tela de mensagens
                        val chatData = chat.toObject(Chat::class.java)
                        val intent = Intent(context, MessageActivity::class.java).apply {
                            putExtra("dadosDestinatarios", user)
                            putExtra("idChat", chatData?.idChat)
                        }
                        startActivity(intent)
                    } else {
                        Log.w("Firestore", "Nenhum chat encontrado para os participantes!")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Erro ao buscar chat: ${exception.message}")
                }
        }
        
        binding.RecyclerViewList.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        return binding.root
    }
    
    override fun onStart() {
        super.onStart()
        loadChats()
    }
    
    private fun loadChats() {
        val currentUserId = firebaseAuth.currentUser?.uid ?: return
        val query = firestore.collection("Chat")
            .whereArrayContains("participants", currentUserId)
        
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val chats = task.result?.documents?.mapNotNull { it.toObject(Chat::class.java) } ?: emptyList()
                val chatList = mutableListOf<Triple<Chat, Vehicle?, String>>()
                
                chats.forEach { chat ->
                    val otherUserId = chat.participants.first { it != currentUserId }
                    fetchUserAndVehicle(otherUserId, chat, chatList, chats.size)
                }
            } else {
                Log.e("ChatFragment", "Erro ao carregar chats: ${task.exception?.message}")
            }
        }
    }
    
    private fun fetchUserAndVehicle(
        otherUserId: String,
        chat: Chat,
        chatList: MutableList<Triple<Chat, Vehicle?, String>>,
        totalChats: Int
    ) {
        firestore.collection("users").document(otherUserId)
            .get()
            .addOnSuccessListener { userDoc ->
                val userName = userDoc.getString("name") ?: "Usuário"
                if (!chat.idVehicle.isNullOrEmpty()) {
                    firestore.collection("vehicle").document(chat.idVehicle!!)
                        .get()
                        .addOnSuccessListener { vehicleDoc ->
                            val vehicle = vehicleDoc.toObject(Vehicle::class.java)
                            addToChatList(chat, vehicle, userName, chatList, totalChats)
                        }
                        .addOnFailureListener {
                            addToChatList(chat, null, userName, chatList, totalChats)
                        }
                } else {
                    addToChatList(chat, null, userName, chatList, totalChats)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ChatFragment", "Erro ao carregar usuário: ${exception.message}")
            }
    }
    
    private fun addToChatList(
        chat: Chat,
        vehicle: Vehicle?,
        userName: String,
        chatList: MutableList<Triple<Chat, Vehicle?, String>>,
        totalChats: Int
    ) {
        if (chatList.none { it.first.idChat == chat.idChat }) {
            chatList.add(Triple(chat, vehicle, userName))
        }
        
        if (chatList.size == totalChats) {
            chatAdapter.submitList(chatList.distinctBy { it.first.idChat })
        }
    }
}
