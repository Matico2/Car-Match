package com.example.carmatch.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carmatch.adapters.ChatAdapter
import com.example.carmatch.model.Chat
import com.example.carmatch.model.Vehicle
import com.example.carmatch.utils.Constants
import com.example.carmatch.view.activitys.MessageActivity
import com.example.carmatch1.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatFragment : Fragment() {
    
    private lateinit var binding: FragmentChatBinding
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var chatAdapter: ChatAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        
        chatAdapter = ChatAdapter { chat, userName ->
            Toast.makeText(context, "Abrindo conversa com: $userName", Toast.LENGTH_SHORT).show()
            
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("dadosDestinatarios", chat)
            intent.putExtra("userName", userName)
            intent.putExtra("origem", Constants.ORIGEM_CONTATO)
            startActivity(intent)
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
        
        toggleLoading(true)
        
        firestore.collection("Chat")
            .whereIn("idUser1", listOf(currentUserId))
            .get()
            .addOnSuccessListener { querySnapshot1 ->
                val chats1 = querySnapshot1.documents.mapNotNull { it.toObject(Chat::class.java) }
                
                firestore.collection("Chat")
                    .whereIn("idUser2", listOf(currentUserId))
                    .get()
                    .addOnSuccessListener { querySnapshot2 ->
                        val chats2 = querySnapshot2.documents.mapNotNull { it.toObject(Chat::class.java) }
                        
                        val allChats = (chats1 + chats2).distinctBy { it.idChat }
                        
                        if (allChats.isEmpty()) {
                            toggleLoading(false)
                            toggleEmptyView(true)
                            return@addOnSuccessListener
                        }
                        
                        val vehicleIds = allChats.map { it.idVehicle }.distinct()
                        val userIds = allChats.map { it.idUser2 }.distinct()
                        
                        firestore.collection("vehicle")
                            .whereIn("idVehicle", vehicleIds)
                            .get()
                            .addOnSuccessListener { vehicleSnapshot ->
                                val vehicles = vehicleSnapshot.documents.associateBy(
                                    { it.id },
                                    { it.toObject(Vehicle::class.java) }
                                )
                                
                                firestore.collection("users")
                                    .whereIn("idUser", userIds)
                                    .get()
                                    .addOnSuccessListener { userSnapshot ->
                                        val users = userSnapshot.documents.associate {
                                            it.id to it["name"] as String
                                        }
                                        
                                        val chatDetails = allChats.map { chat ->
                                            val vehicle = vehicles[chat.idVehicle]
                                            Triple(
                                                chat,
                                                vehicle,
                                                users[chat.idUser2] ?: "Usuário desconhecido"
                                            )
                                        }
                                        
                                        chatAdapter.submitList(chatDetails)
                                        toggleLoading(false)
                                        toggleEmptyView(chatDetails.isEmpty())
                                    }
                                    .addOnFailureListener {
                                        Log.e("ChatFragment", "Erro ao buscar usuários: ${it.message}")
                                        toggleLoading(false)
                                        toggleEmptyView(true)
                                    }
                            }
                            .addOnFailureListener {
                                Log.e("ChatFragment", "Erro ao buscar veículos: ${it.message}")
                                toggleLoading(false)
                                toggleEmptyView(true)
                            }
                    }
                    .addOnFailureListener {
                        Log.e("ChatFragment", "Erro ao buscar chats como idUser2: ${it.message}")
                        toggleLoading(false)
                        toggleEmptyView(true)
                    }
            }
            .addOnFailureListener {
                Log.e("ChatFragment", "Erro ao buscar chats como idUser1: ${it.message}")
                toggleLoading(false)
                toggleEmptyView(true)
            }
    }
    
    private fun toggleLoading(isLoading: Boolean) {
        //binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    
    private fun toggleEmptyView(isEmpty: Boolean) {
        binding.emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}
