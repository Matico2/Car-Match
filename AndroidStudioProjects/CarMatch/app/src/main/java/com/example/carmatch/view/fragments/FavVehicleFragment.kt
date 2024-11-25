package com.example.carmatch.view.fragments

import FavVehicleAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carmatch.model.Vehicle
import com.example.carmatch.view.activitys.DetailsVehicleActivity
import com.example.carmatch1.databinding.FragmentFavVehicleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class FavVehicleFragment : Fragment(), FavVehicleAdapter.OnClickListener {
    
    private lateinit var binding: FragmentFavVehicleBinding
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var favVehiclesAdapter: FavVehicleAdapter
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private var eventSnapshot: ListenerRegistration? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar o layout do fragmento usando ViewBinding
        binding = FragmentFavVehicleBinding.inflate(inflater, container, false)
        
        // Configurar o RecyclerView com um LayoutManager e Adaptador
        binding.RecyclerViewList.layoutManager = LinearLayoutManager(requireContext())
        favVehiclesAdapter = FavVehicleAdapter(this)
        binding.RecyclerViewList.adapter = favVehiclesAdapter
        
        return binding.root
    }
    
    
    
    
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFavoriteVehicles()
    }
    
    private fun setupRecyclerView() {
        binding.RecyclerViewList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favVehiclesAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
    }
    
    private fun loadFavoriteVehicles() {
        val currentUserId = firebaseAuth.currentUser?.uid
        if (currentUserId == null) {
            Log.e("FavVehicleFragment", "Usuário não autenticado.")
            return
        }
        
        eventSnapshot = firestore
            .collection("FavVehicle")
            .whereEqualTo("userUID", currentUserId)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    Log.e("FavVehicleFragment", "Erro ao carregar favoritos: ${error.message}")
                    return@addSnapshotListener
                }
                
                val list = querySnapshot?.documents?.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(Vehicle::class.java)
                } ?: emptyList()
                
                favVehiclesAdapter.addList(list)
            }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        eventSnapshot?.remove()
    }
    
    override fun onVehicleClick(vehicleId: String) {
        val intent = Intent(requireContext(), DetailsVehicleActivity::class.java)
        intent.putExtra("vehicleId", vehicleId)
        startActivity(intent)
    }
    
    override fun onRemoveFavoriteClick(vehicleId: String) {
        firestore.collection("FavVehicle").document(vehicleId).delete()
            .addOnSuccessListener {
                Log.d("FavVehicleFragment", "Veículo removido dos favoritos.")
            }
            .addOnFailureListener { error ->
                Log.e("FavVehicleFragment", "Erro ao remover veículo: ${error.message}")
            }
    }
    
    override fun onChatClick(chatId: String) {
        // Implementar abertura do chat
    }
}
