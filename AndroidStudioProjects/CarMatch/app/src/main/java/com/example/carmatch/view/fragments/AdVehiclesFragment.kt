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

class AdVehiclesFragment : Fragment(), AdVehiclesAdapter.OnItemClickListener {
    
    private lateinit var binding: FragmentAdVehiclesBinding
    private lateinit var adVehiclesAdapter: AdVehiclesAdapter
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val listUser = ArrayList<User>()
    private val listVehicle = ArrayList<Vehicle>()
    private val listAd = ArrayList<AdVehicle>()
    private val filteredListAd = ArrayList<AdVehicle>()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdVehiclesBinding.inflate(inflater, container, false)
        adVehiclesAdapter = AdVehiclesAdapter(filteredListAd, listUser, listVehicle, this)
        binding.RecyclerViewList.adapter = adVehiclesAdapter
        binding.RecyclerViewList.layoutManager = LinearLayoutManager(requireContext())
        
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText ?: "")
                return true
            }
        })
        
        return binding.root
    }
    
    override fun onStart() {
        super.onStart()
        Log.d("Firestore", "Iniciando carregamento de anúncios...")
        loadAds()
    }
    
    
    private fun loadAds() {
        val currentUserId = firebaseAuth.currentUser?.uid ?: return
        firestore.collection("AdVehicle")
            .whereEqualTo("status", true)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Error fetching ads: ${error.message}", error)
                    return@addSnapshotListener
                }
                if (querySnapshot != null) {
                    listAd.clear()
                    filteredListAd.clear()
                    querySnapshot.documents.forEach { document ->
                        val ad = document.toObject(AdVehicle::class.java)
                        if (ad != null && ad.idUser != currentUserId) {
                            listAd.add(ad)
                            loadVehicleAndUser(ad)
                        }
                    }
                    Log.d("Firestore", "Anúncios carregados: ${listAd.size}")
                    // Atualiza o adaptador caso nenhum anúncio tenha sido carregado
                    if (listAd.isEmpty()) {
                        Log.d("Firestore", "Nenhum anúncio disponível.")
                        adVehiclesAdapter.notifyDataSetChanged()
                    }
                }
            }
    }
    
    
    
    private fun loadVehicleAndUser(ad: AdVehicle) {
        firestore.collection("vehicle").document(ad.idVehicle).get()
            .addOnSuccessListener { vehicleDoc ->
                val vehicle = vehicleDoc.toObject(Vehicle::class.java)
                if (vehicle != null) {
                    listVehicle.add(vehicle)
                    firestore.collection("users").document(ad.idUser).get()
                        .addOnSuccessListener { userDoc ->
                            val user = userDoc.toObject(User::class.java)
                            if (user != null) {
                                listUser.add(user)
                                Log.d("Firestore", "Veículo carregado: ${vehicle.vehicleId}")
                                Log.d("Firestore", "Usuário carregado: ${user.idUser}")
                                if (listAd.size == listUser.size && listAd.size == listVehicle.size) {
                                    filteredListAd.clear()
                                    filteredListAd.addAll(listAd)
                                    adVehiclesAdapter.notifyDataSetChanged()
                                    Log.d("Firestore", "Anúncios exibidos: ${filteredListAd.size}")
                                }
                            }
                        }
                } else {
                    Log.w("Firestore", "Veículo não encontrado para o anúncio: ${ad.idAd}")
                    // Remova o anúncio correspondente da lista
                    listAd.remove(ad)
                    filteredListAd.clear()
                    filteredListAd.addAll(listAd)
                    adVehiclesAdapter.notifyDataSetChanged()
                }
            }
    }
    
    
    
    fun showSelectedVehicle(vehicleId: String) {
        val selectedAd = listAd.find { ad -> ad.idVehicle == vehicleId }
        if (selectedAd != null) {
            filteredListAd.clear()
            filteredListAd.add(selectedAd)
            adVehiclesAdapter.notifyDataSetChanged()
        } else {
            Log.e("AdVehiclesFragment", "Veículo com ID $vehicleId não encontrado.")
        }
    }
    
    
    private fun filterList(query: String) {
        filteredListAd.clear()
        val lowerCaseQuery = query.lowercase()
     
        for (ad in listAd) {
            val vehicle = listVehicle.find { it.vehicleId == ad.idVehicle }
            val user = listUser.find { it.idUser == ad.idUser }
            
            if (vehicle != null && user != null) {
                if (vehicle.model.lowercase().contains(lowerCaseQuery) ||
                    user.name.lowercase().contains(lowerCaseQuery)
                ) {
                    filteredListAd.add(ad)
                }
            }
        }
        
        adVehiclesAdapter.notifyDataSetChanged()
    }
    
    override fun onItemClick(adVehicle: AdVehicle) {
        val currentUserId = firebaseAuth.currentUser?.uid ?: return
        firestore.collection("Chat")
            .whereIn("idUser1", listOf(currentUserId, adVehicle.idUser))
            .whereIn("idUser2", listOf(currentUserId, adVehicle.idUser))
            .whereEqualTo("idAd", adVehicle.idAd)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    val chatId = querySnapshot.documents[0].id
                    navigateToChat(chatId)
                } else {
                    createChat(currentUserId, adVehicle)
                }
            }
    }
    
    private fun createChat(currentUserId: String, adVehicle: AdVehicle) {
        val chat = Chat(
            idChat = "",
            idUser1 = currentUserId,
            idUser2 = adVehicle.idUser,
            idVehicle = adVehicle.idVehicle,
            idAd = adVehicle.idAd,
            status = true,
            participants = listOf(currentUserId, adVehicle.idUser)
        )
        
        firestore.collection("Chat").add(chat)
            .addOnSuccessListener { document ->
                val chatId = document.id
                firestore.collection("Chat").document(chatId).update("idChat", chatId)
                    .addOnSuccessListener {
                        navigateToChat(chatId)
                    }
            }
    }
    
    private fun navigateToChat(chatId: String) {
        Log.d("AdVehiclesFragment", "Navigating to ChatActivity with chatId: $chatId")
        val intent = Intent(requireContext(), ChatActivity::class.java).apply {
            putExtra("chatId", chatId)
        }
        startActivity(intent)
    }
}

