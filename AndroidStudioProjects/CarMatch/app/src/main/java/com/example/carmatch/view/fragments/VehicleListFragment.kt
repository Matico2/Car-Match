package com.example.carmatch.view.fragments

import android.content.Context
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
import com.example.carmatch.adapters.VehiclesAdapter
import com.example.carmatch.model.AdVehicle
import com.example.carmatch.model.Vehicle
import com.example.carmatch.utils.showMenssage
import com.example.carmatch.view.DetailsVehicleActivity
import com.example.carmatch1.databinding.FragmentVehicleListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class VehicleListFragment : Fragment(), VehiclesAdapter.OnItemClickListener {
    
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var binding: FragmentVehicleListBinding
    private lateinit var eventSnapShot: ListenerRegistration
    private lateinit var vehiclesAdapter: VehiclesAdapter
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVehicleListBinding.inflate(inflater, container, false)
        vehiclesAdapter = VehiclesAdapter(this)
        binding.RecyclerViewList.adapter = vehiclesAdapter
        binding.RecyclerViewList.layoutManager = LinearLayoutManager(context)
        binding.RecyclerViewList.addItemDecoration(
            DividerItemDecoration(
                context, LinearLayoutManager.VERTICAL
            )
        )
        return binding.root
    }
    override fun onAdvertiseClick(vehicle: Vehicle) {
        Log.d("VehicleListFragment", "Botão de anúncio clicado para veículo: ${vehicle.vehicleId}")
        adVehicle(vehicle)
    }
    
    private fun adVehicle(vehicle: Vehicle) {
        val idUser = firebaseAuth.currentUser?.uid ?: run {
            Log.e("VehicleListFragment", "Usuário não autenticado.")
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