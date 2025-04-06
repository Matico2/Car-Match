package com.example.carmatch.view.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carmatch.adapters.FavVehicleAdapter
import com.example.carmatch.model.FavVehicle
import com.example.carmatch.model.Vehicle
import com.example.carmatch.view.activitys.DetailsVehicleActivity
import com.example.carmatch.view.activitys.MainActivity
import com.example.carmatch.view.activitys.MessageActivity
import com.example.carmatch1.databinding.FragmentFavVehicleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavVehicleFragment : Fragment(), FavVehicleAdapter.OnClickListener {
    
    private lateinit var binding: FragmentFavVehicleBinding
    private lateinit var favVehicledapter: FavVehicleAdapter
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val listFavVehicle = mutableListOf<FavVehicle>()
    private val listVehicle = mutableListOf<Vehicle>()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d("FavVehicleFragment", "onCreateView chamado")
        binding = FragmentFavVehicleBinding.inflate(inflater, container, false)
        favVehicledapter = FavVehicleAdapter(context,listFavVehicle, listVehicle,this)// erro aqui
        binding.RecyclerViewListFav.adapter = favVehicledapter
        binding.RecyclerViewListFav.layoutManager = LinearLayoutManager(requireContext())
        
        return binding.root
    }
    
    override fun onStart() {
        super.onStart()
        Log.d("FavVehicleFragment", "onStart chamado")
        loadFavoriteVehicles()
    }
    
    private fun loadFavoriteVehicles() {
        val currentUserId = firebaseAuth.currentUser?.uid
        if (currentUserId == null) {
            Log.e("FavVehicleFragment", "Usuário não está logado")
            return
        }
        
        firestore.collection("favVehicle")
            .whereEqualTo("status", true)
            .whereEqualTo("userId", currentUserId)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    Log.e("FavVehicleFragment", "Erro ao buscar favoritos: ${error.message}")
                    return@addSnapshotListener
                }
                if (querySnapshot != null) {
                    listFavVehicle.clear()
                    listVehicle.clear() // Limpa a lista de veículos para evitar duplicação
                    querySnapshot.documents.forEach { document ->
                        val fav = document.toObject(FavVehicle::class.java)
                        if (fav != null) {
                            listFavVehicle.add(fav)
                            loadVehicle(fav)
                        }
                    }
                    favVehicledapter.notifyDataSetChanged() // Atualizar o adapter depois de modificar as listas
                }
            }
    }
    
    
    private fun loadVehicle(fav: FavVehicle) {
        firestore.collection("vehicle").document(fav.idVehicle).get()
            .addOnSuccessListener { vehicleDoc ->
                Log.d("FavVehicleFragment", "Documento recebido: ${vehicleDoc.id}")
                val vehicle = vehicleDoc.toObject(Vehicle::class.java)
                if (vehicle != null) {
                    listVehicle.add(vehicle)
                    favVehicledapter.notifyDataSetChanged()
                }
            }
    }
    
    
    override fun onVehicleClick(vehicleId: String) {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.putExtra("selectedVehicleId", vehicleId)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }
    
    
    override fun onRemoveFavoriteClick(vehicleId: String) {
        firestore.collection("favVehicle")
            .whereEqualTo("idVehicle", vehicleId)
            .get()
            .addOnSuccessListener { snapshot ->
                for (doc in snapshot.documents) {
                    firestore.collection("favVehicle").document(doc.id).delete()
                }
                Log.d("FavVehicleFragment", "Veículo removido dos favoritos: $vehicleId")
                loadFavoriteVehicles()
            }
            .addOnFailureListener { error ->
                Log.e("FavVehicleFragment", "Erro ao remover veículo: ${error.message}")
            }
    }
    
    override fun onChatClick(vehicleId: String, userId: String?) {
        val currentUserId = firebaseAuth.currentUser?.uid ?: return
        val chatId = if (currentUserId < userId.toString()) "$currentUserId-$userId" else "$userId-$currentUserId"
        
        val chatData = mapOf(
            "idChat" to chatId,
            "participants" to listOf(currentUserId, userId),
            "vehicleId" to vehicleId,
            "timestamp" to System.currentTimeMillis()
        )
        
        firestore.collection("Chat").document(chatId).get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    firestore.collection("Chat").document(chatId).set(chatData)
                        .addOnSuccessListener {
                            if (userId != null) {
                                openChatActivity(chatId, userId)
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("FavVehicleFragment", "Erro ao criar o chat: ${e.message}")
                        }
                } else {
                    if (userId != null) {
                        openChatActivity(chatId, userId)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("FavVehicleFragment", "Erro ao verificar o chat: ${e.message}")
            }
    }
    
    private fun openChatActivity(chatId: String, userId: String) {
        val intent = Intent(requireContext(), MessageActivity::class.java).apply {
            putExtra("idChat", chatId)
            putExtra("userId", userId)
        }
        startActivity(intent)
    }
    
}
