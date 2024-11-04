package com.example.carmatch.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carmatch.model.AdVehicle
import com.example.carmatch.view.adapters.VehicleImagesAdapter
import com.example.carmatch1.databinding.FragmentAdVehiclesBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class AdVehiclesFragment : Fragment() {
    
    private lateinit var binding: FragmentAdVehiclesBinding
    private val db = FirebaseFirestore.getInstance()
    private val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
    private val vehicleList = mutableListOf<AdVehicle>()
    private lateinit var adapter: VehicleImagesAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdVehiclesBinding.inflate(inflater, container, false)
        
        setupRecyclerView()
        loadOtherUsersVehicles()
        return binding.root
    }
    
    private fun setupRecyclerView() {
        adapter = VehicleImagesAdapter(vehicleList)
        binding.vehicleImagesRecyclerView.apply { // Certifique-se de que este é o ID correto do RecyclerView no seu layout
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
    
    private fun loadOtherUsersVehicles() {
        db.collection("advehicles")
            .whereNotEqualTo("idUser", currentUserID)
            .whereEqualTo("status", true)
            .get()
            .addOnSuccessListener { documents ->
                vehicleList.clear()
                for (document in documents) {
                    val vehicle = document.toObject(AdVehicle::class.java)
                    vehicleList.add(vehicle)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}
