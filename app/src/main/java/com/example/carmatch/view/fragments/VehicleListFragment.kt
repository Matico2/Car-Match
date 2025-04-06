package com.example.carmatch.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carmatch.adapters.VehiclesAdapter
import com.example.carmatch.model.AdVehicle
import com.example.carmatch.model.Vehicle
import com.example.carmatch.view.activitys.DetailsVehicleActivity
import com.example.carmatch1.databinding.FragmentVehicleListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class VehicleListFragment : Fragment(), VehiclesAdapter.OnItemClickListener {
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var binding: FragmentVehicleListBinding
    private lateinit var eventSnapShot: ListenerRegistration
    private lateinit var vehiclesAdapter: VehiclesAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVehicleListBinding.inflate(inflater, container, false)
        vehiclesAdapter = VehiclesAdapter(this)
        
        binding.RecyclerViewList.adapter = vehiclesAdapter
        binding.RecyclerViewList.layoutManager = LinearLayoutManager(context)
        binding.RecyclerViewList.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        )
        
        // Adicionando o listener na SearchView
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { filterVehicles(it) }
                return true
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterVehicles(it) }
                return true
            }
        })
        
        return binding.root
    }
    
    interface OnItemClickListener {
        fun onItemClick(vehicle: Vehicle)
        fun onAdvertiseClick(vehicle: Vehicle) }
    
    private var originalVehicleList = listOf<Vehicle>()
    
    private fun filterVehicles(query: String) {
        val filteredList = originalVehicleList.filter { vehicle ->
            val brandMatches = vehicle.brand.contains(query, ignoreCase = true)
            val priceMatches = vehicle.price.toString().contains(query, ignoreCase = true)
            brandMatches || priceMatches
        }
        vehiclesAdapter.addList(filteredList)
    }
    
    private fun adVehicle(vehicle: Vehicle) {
        val idUser = firebaseAuth.currentUser?.uid ?: run {
            return
        }
        val adId = firestore.collection("AdVehicle").document().id
        val adVehicle = AdVehicle(idUser = idUser, idVehicle = vehicle.vehicleId, idAd = adId, status = true)
        
        firestore.collection("AdVehicle").document(adId)
            .set(adVehicle)
            .addOnSuccessListener {
                showMenssage(requireContext(), "Anúncio criado com sucesso!")
            }
            .addOnFailureListener { e ->
                showMenssage(requireContext(), "Erro ao criar anúncio.")
            }
    }
    
    override fun onItemClick(vehicle: Vehicle) {
        val intent = Intent(requireContext(), DetailsVehicleActivity::class.java).apply {
            putExtra("vehicleId", vehicle.vehicleId)
            putExtra("userUID", vehicle.userUID)
        }
        startActivity(intent)
    }
    
    override fun onStart() {
        super.onStart()
        listenerVehicles()
    }
    
    private fun listenerVehicles() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        eventSnapShot = firestore
            .collection("vehicle")
            .whereEqualTo("userUID", currentUserId)
            .addSnapshotListener { querySnapshot, error ->
                val list = mutableListOf<Vehicle>()
                val doc = querySnapshot?.documents
                doc?.forEach { documentSnapshot ->
                    val vehicle = documentSnapshot.toObject(Vehicle::class.java)
                    if (vehicle != null) {
                        list.add(vehicle)
                    }
                }
                if (list.isNotEmpty()) {
                    originalVehicleList = list // Atualiza a lista original
                    vehiclesAdapter.addList(list)
                }
            }
    }
    
    
    override fun onDestroy() {
        super.onDestroy()
        eventSnapShot.remove()
    }
    
    fun showMenssage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}