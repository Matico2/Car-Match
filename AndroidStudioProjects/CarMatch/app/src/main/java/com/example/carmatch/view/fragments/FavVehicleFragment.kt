package com.example.carmatch.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carmatch.adapters.FavVehicleAdapter
import com.example.carmatch.model.Vehicle
import com.example.carmatch1.databinding.FragmentFavVehicleBinding
import com.google.firebase.firestore.FirebaseFirestore

class FavVehicleFragment : Fragment(), FavVehicleAdapter.OnClickListener {
    
    private var _binding: FragmentFavVehicleBinding? = null
    private val binding get() = _binding!!
    private lateinit var favVehicleAdapter: FavVehicleAdapter
    private val db = FirebaseFirestore.getInstance()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavVehicleBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        loadFavoriteVehicles()
    }
    
    private fun setupRecyclerView() {
        favVehicleAdapter = FavVehicleAdapter(this)
        binding.RecyclerViewList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favVehicleAdapter
        }
    }
    
    private fun loadFavoriteVehicles() {
        val userId = "user_id" // Substitua pelo ID do usuário atual
        db.collection("favorites").whereEqualTo("userId", userId).get()
            .addOnSuccessListener { documents ->
                val favoriteVehicles = documents.mapNotNull { it.toObject(Vehicle::class.java) }
                favVehicleAdapter.submitList(favoriteVehicles)
            }
            .addOnFailureListener { exception ->
                // Trate a falha de carregamento
            }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    override fun onVehicleClick(vehicleId: String) {
        // Ação ao clicar em um veículo favorito
    }
}
