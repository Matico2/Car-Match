package com.example.carmatch.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carmatch.adapters.ChatAdapter
import com.example.carmatch1.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.example.carmatch.model.User

class ChatFragment : Fragment() {
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var eventSnapShot: ListenerRegistration
    private lateinit var binding: FragmentChatBinding
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(
            inflater, container, false
        )
        chatAdapter = ChatAdapter()
        binding.RecyclerViewList.adapter = chatAdapter
        binding.RecyclerViewList.layoutManager = LinearLayoutManager(context)
        binding.RecyclerViewList.addItemDecoration(
            DividerItemDecoration(
                context, LinearLayoutManager.VERTICAL
            )
        )
        return binding.root
    }
    
    override fun onStart() {
        super.onStart()
        addListenerChat()
    }
    
    private fun addListenerChat() {
        eventSnapShot = firestore
            .collection("users")
            .addSnapshotListener { querySnapShot, error ->
                val doc = querySnapShot?.documents
                val listUsers = mutableListOf<User>()
                doc?.forEach { documentSnapshot ->
                    val user =
                        documentSnapshot.toObject(com.example.carmatch.model.User::class.java)
                    val userLogado = firebaseAuth.currentUser?.uid
                    if (user != null && userLogado != null) {
                        if (userLogado != user.idUser) {
                            listUsers.add(user)
                        }
                    }
                }
               if(listUsers.isNotEmpty() ) {
                   chatAdapter.addList(listUsers)
               }
            }
        
    }
    
    override fun onDestroy() {
        super.onDestroy()
        eventSnapShot.remove()
    }
}

