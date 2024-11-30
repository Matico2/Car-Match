import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carmatch.model.FavVehicle
import com.example.carmatch.model.Vehicle
import com.example.carmatch.view.activitys.DetailsVehicleActivity
import com.example.carmatch1.databinding.FragmentFavVehicleBinding
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class FavVehicleFragment : Fragment(), FavVehicleAdapter.OnClickListener {
    
    private lateinit var binding: FragmentFavVehicleBinding
    private lateinit var adapter: FavVehicleAdapter
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val listFavVehicle = mutableListOf<FavVehicle>()
    private val listVehicle = mutableListOf<Vehicle>()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavVehicleBinding.inflate(inflater, container, false)
        
        adapter = FavVehicleAdapter(listFavVehicle, listVehicle, this)
        binding.recyclerViewListFav.adapter = adapter
        binding.recyclerViewListFav.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewListFav.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        )
        
        return binding.root
    }
    
    override fun onStart() {
        super.onStart()
        loadFavoriteVehicles()
    }
    
    private fun loadFavoriteVehicles() {
        val currentUserId = firebaseAuth.currentUser?.uid ?: return
        firestore.collection("favVehicle")
            .whereEqualTo("userUID", currentUserId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FavVehicleFragment", "Erro ao carregar favoritos", error)
                    return@addSnapshotListener
                }
                
                if (snapshot != null && !snapshot.isEmpty) {
                    listFavVehicle.clear()
                    snapshot.documents.mapNotNullTo(listFavVehicle) { it.toObject(FavVehicle::class.java) }
                    fetchVehiclesForFavorites()
                } else {
                    Log.d("FavVehicleFragment", "Nenhum favorito encontrado")
                    listFavVehicle.clear()
                    adapter.notifyDataSetChanged()
                }
            }
    }
    
    private fun fetchVehiclesForFavorites() {
        if (listFavVehicle.isEmpty()) return
        
        val fetchTasks = listFavVehicle.map { fav ->
            firestore.collection("vehicle").document(fav.idVehicle).get()
        }
        
        Tasks.whenAllSuccess<DocumentSnapshot>(fetchTasks)
            .addOnSuccessListener { documents ->
                listVehicle.clear()
                documents.mapNotNullTo(listVehicle) { it.toObject(Vehicle::class.java) }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { error ->
                Log.e("FavVehicleFragment", "Erro ao buscar veículos: ${error.message}")
            }
    }
    
    override fun onVehicleClick(vehicleId: String) {
        val intent = Intent(requireContext(), DetailsVehicleActivity::class.java)
        intent.putExtra("vehicleId", vehicleId)
        startActivity(intent)
    }
    
    override fun onRemoveFavoriteClick(vehicleId: String) {
        firestore.collection("favVehicle").document(vehicleId).delete()
            .addOnSuccessListener {
                Log.d("FavVehicleFragment", "Veículo removido dos favoritos: $vehicleId")
            }
            .addOnFailureListener { error ->
                Log.e("FavVehicleFragment", "Erro ao remover veículo: ${error.message}")
            }
    }
    
    override fun onChatClick(chatId: String) {
        // Implementar lógica de chat
    }
}
