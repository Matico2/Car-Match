package com.example.carmatch.view.activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.carmatch.adapters.ImagePagerAdapter
import com.example.carmatch.model.Vehicle
import com.example.carmatch1.databinding.ActivityDetailsVehicleBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class DetailsVehicleActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailsVehicleBinding.inflate(layoutInflater) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        includeToolbarApp()
        
        val vehicleId = intent.getStringExtra("vehicleId")
        val userUID = intent.getStringExtra("userUID")
        
        loadVehicleDetails(userUID, vehicleId)
        initializeEventClick(vehicleId)
    }
    
    private fun initializeEventClick(vehicleId: String?) {
        binding.btnEditVehicle.setOnClickListener {
            val intent = Intent(this, EditVehicleActivity::class.java).apply {
                putExtra("vehicleId", vehicleId)
            }
            startActivity(intent)
        }
        binding.btnDeleteVehicle.setOnClickListener {
            vehicleId?.let { id ->
                deleteVehicle(id)
                
            }
        }
    }
    
    private fun deleteVehicle(vehicleId: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar Exclusão")
        builder.setMessage("Você realmente deseja deletar este veículo?")
        
        builder.setPositiveButton("Sim") { _, _ ->
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("vehicle")
                .document(vehicleId)
                .delete()
                .addOnSuccessListener {
                    Log.d("Delete Vehicle", "Vehicle successfully deleted!")
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.e("Delete Vehicle", "Error deleting vehicle: ", exception)
                }
        }
        
        builder.setNegativeButton("Não") { dialog, _ ->
            dialog.dismiss()
        }
        
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    
    
    private fun loadVehicleDetails(userUID: String?, vehicleId: String?) {
        if (userUID != null && vehicleId != null) {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("vehicle")
                .document(vehicleId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val vehicle = document.toObject(Vehicle::class.java)
                        vehicle?.let {
                            binding.txtBrandModel.text = "${it.brand} ${it.model}"
                            binding.txtYearFuel.text = "${it.year} - ${it.fuelType}"
                            binding.txtPrice.text = "${it.price}"
                            binding.txtKmCondition.text = "${it.km} - ${it.condition}"
                            binding.txtPlate.text = it.plate
                            binding.txtType.text = it.type
                            binding.txtDescription.text = it.description
                            loadImages(userUID, vehicleId)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore Error", "Error getting documents: ", exception)
                }
        }
    }
    
    private fun loadImages(userUID: String, vehicleId: String) {
        val storageReference = FirebaseStorage.getInstance().reference.child("photos/vehicles/$userUID/$vehicleId")
        val imagePaths = mutableListOf<String>()
        
        // Listar todas as imagens na pasta do veículo
        storageReference.listAll()
            .addOnSuccessListener { listResult ->
                listResult.items.forEach { item ->
                    item.downloadUrl.addOnSuccessListener { uri ->
                        imagePaths.add(uri.toString())
                        if (imagePaths.size == listResult.items.size) {
                            // Configurar o ViewPager assim que todas as URLs forem carregadas
                            val adapter = ImagePagerAdapter(this, imagePaths)
                            binding.viewPagerImages.adapter = adapter
                            setupViewPager()
                        }
                    }.addOnFailureListener {
                        Log.e("ImageError", "Erro ao carregar URL da imagem: ${it.message}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("StorageError", "Erro ao listar imagens no Firebase Storage: ${exception.message}")
            }
    }
    
    // Configurar o ViewPager para loop
    private fun setupViewPager() {
        binding.viewPagerImages.apply {
            clipToPadding = false
            setPadding(48, 0, 48, 0)
            pageMargin = 16
        }
    }
    
    
    private fun includeToolbarApp() {
        val toolbar = binding.includeMainToolbar.toolbarApp
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Veículo"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}
