package com.example.carmatch.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carmatch.adapters.AdVehiclesAdapter
import com.example.carmatch.model.AdVehicle
import com.example.carmatch.model.Chat
import com.example.carmatch.model.User
import com.example.carmatch.model.Vehicle
import com.example.carmatch.view.activitys.ChatActivity
import com.example.carmatch1.databinding.FragmentAdVehiclesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class AdVehiclesFragment : Fragment(), AdVehiclesAdapter.OnItemClickListener {
    
    private lateinit var binding: FragmentAdVehiclesBinding
    private lateinit var adVehiclesAdapter: AdVehiclesAdapter
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var eventSnapShot: ListenerRegistration
    private val listUser = ArrayList<User>()
    private val listVehicle = ArrayList<Vehicle>()
    private val listAd = ArrayList<AdVehicle>()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdVehiclesBinding.inflate(inflater, container, false)
        adVehiclesAdapter = AdVehiclesAdapter(listAd, listUser, listVehicle, this)
        binding.RecyclerViewList.adapter = adVehiclesAdapter
        binding.RecyclerViewList.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }
    
    override fun onStart() {
        super.onStart()
        listenerAds()
    }
    
    private fun listenerAds() {
        val currentUserId = firebaseAuth.currentUser?.uid
        firestore.collection("AdVehicle")
            .whereEqualTo("status", true)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    Log.e("Firestore Error", "Error fetching ads: ${error.message}", error)
                    return@addSnapshotListener
                }
                if (querySnapshot != null) {
                    Log.d("Firestore", "Documents fetched: ${querySnapshot.size()}")
                    listAd.clear()
                    listUser.clear()
                    listVehicle.clear()
                    for (adDocument in querySnapshot.documents) {
                        val adVehicle = adDocument.toObject(AdVehicle::class.java)
                        if (adVehicle != null && adVehicle.idUser != currentUserId) {
                            listAd.add(adVehicle)
                            fetchVehicleAndUser(adVehicle) { isFetched ->
                                if (isFetched && listAd.size == listUser.size && listAd.size == listVehicle.size) {
                                    adVehiclesAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
            }
    }
    
    private fun fetchVehicleAndUser(adVehicle: AdVehicle, onComplete: (Boolean) -> Unit) {
        firestore.collection("vehicle").document(adVehicle.idVehicle)
            .get()
            .addOnSuccessListener { vehicleDoc ->
                val vehicle = vehicleDoc.toObject(Vehicle::class.java)
                if (vehicle != null) {
                    listVehicle.add(vehicle)
                    firestore.collection("users").document(adVehicle.idUser)
                        .get()
                        .addOnSuccessListener { userDoc ->
                            val user = userDoc.toObject(User::class.java)
                            if (user != null) {
                                listUser.add(user)
                                onComplete(true)
                            } else {
                                Log.e(
                                    "Firestore Error",
                                    "User not found for ID: ${adVehicle.idUser}"
                                )
                                onComplete(false)
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore Error", "Error fetching user: ", e)
                            onComplete(false)
                        }
                } else {
                    Log.e("Firestore Error", "Vehicle not found for ID: ${adVehicle.idVehicle}")
                    onComplete(false)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore Error", "Error fetching vehicle: ", e)
                onComplete(false)
            }
    }
    
    override fun onItemClick(adVehicle: AdVehicle) {
        val currentUserId = firebaseAuth.currentUser?.uid ?: run {
            Log.e("Error", "Usuário não autenticado!")
            return
        }
        
        firestore.collection("Chat")
            .whereEqualTo("idUser1", currentUserId)
            .whereEqualTo("idUser2", adVehicle.idUser)
            .whereEqualTo("idAd", adVehicle.idAd)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    val chatId = querySnapshot.documents[0].id
                    navigateToChat(chatId)
                } else {
                    val chat = Chat(
                        idChat = "",  // Deixe o ID em branco para gerar automaticamente
                        idUser1 = currentUserId,
                        idUser2 = adVehicle.idUser,
                        idVehicle = adVehicle.idVehicle,
                        idAd = adVehicle.idAd,
                        status = true,
                        participants = listOf(currentUserId, adVehicle.idUser) // Certifique-se que 'participants' é uma lista de strings
                    )
                    firestore.collection("Chat")
                        .add(chat)
                        .addOnSuccessListener { documentReference ->
                            val newChatId = documentReference.id
                            firestore.collection("Chat").document(newChatId).update("idChat", newChatId)
                                .addOnSuccessListener {
                                    Log.d("Firestore", "Chat criado com sucesso!")
                                    navigateToChat(newChatId)
                                }
                                .addOnFailureListener { e ->
                                    Log.e("Firestore Error", "Erro ao atualizar ID do chat: ${e.message}", e)
                                }
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore Error", "Erro ao criar o chat: ${e.message}", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore Error", "Erro ao verificar chat existente: ${e.message}", e)
            }
    }
    
    
    private fun navigateToChat(chatId: String) {
        val intent = Intent(requireContext(), ChatActivity::class.java).apply {
            putExtra("chatId", chatId)
        }
        startActivity(intent)
    }
    
    
    override fun onDestroy() {
        super.onDestroy()
        if (::eventSnapShot.isInitialized) {
            eventSnapShot.remove()
        }
    }
}
