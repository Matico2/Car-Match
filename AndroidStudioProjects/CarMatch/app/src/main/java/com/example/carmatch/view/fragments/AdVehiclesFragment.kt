package com.example.carmatch.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carmatch.adapters.AdVehiclesAdapter
import com.example.carmatch.model.AdVehicle
import com.example.carmatch.model.User
import com.example.carmatch.model.Vehicle
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
        binding.vehicleCardView.adapter = adVehiclesAdapter
        binding.vehicleCardView.layoutManager = LinearLayoutManager(requireContext())
        binding.vehicleCardView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        return binding.root
    }
    
    override fun onStart() {
        super.onStart()
        listenerAds()
    }
    
    private fun listenerAds() {
        val currentUserId = firebaseAuth.currentUser?.uid
        eventSnapShot = firestore
            .collection("AdVehicle")
            .whereEqualTo("status", true)
            .whereNotEqualTo("idUser", currentUserId)
            .addSnapshotListener { querySnapshot, error ->
                if (querySnapshot != null) {
                    listAd.clear()
                    listUser.clear()
                    listVehicle.clear()
                    
                    for (adDocument in querySnapshot.documents) {
                        val adVehicle = adDocument.toObject(AdVehicle::class.java)
                        if (adVehicle != null) {
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
                                Log.e("Firestore Error", "User not found")
                                onComplete(false)
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore Error", "Error fetching user: ", e)
                            onComplete(false)
                        }
                }
            }
        
    }
    
    override fun onItemClick(adVehicle: AdVehicle) {
        // Implementação do clique no item do anúncio
    }
    
    override fun onDestroy() {
        super.onDestroy()
        eventSnapShot.remove()
    }
}

