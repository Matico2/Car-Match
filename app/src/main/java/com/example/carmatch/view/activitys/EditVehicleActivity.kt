package com.example.carmatch.view.activitys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.carmatch.model.Vehicle
import com.example.carmatch.utils.showMenssage
import com.example.carmatch1.databinding.ActivityEditVehicleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditVehicleActivity : AppCompatActivity() {
    private val binding by lazy { ActivityEditVehicleBinding.inflate(layoutInflater) }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        includeToolbarApp()
        initializeEventClick()
        
        // Verifica se um vehicleId foi passado pela Intent
        val vehicleId = intent.getStringExtra("vehicleId")
        if (vehicleId != null) {
            initializeEditVehicle(vehicleId)  // Carrega os dados do veículo existente
        }
    }
    
    private fun includeToolbarApp() {
        val toolbar = binding.includeToolbarApp.toolbarApp
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Salvar veículo"
            setDisplayHomeAsUpEnabled(true)
        }
    }
    
    private fun initializeEditVehicle(vehicleId: String) {
        firestore.collection("vehicle")
            .document(vehicleId)
            .get()
            .addOnSuccessListener { document ->
                document?.toObject(Vehicle::class.java)?.let { vehicle ->
                    // Preenche os campos de edição com os dados do veículo
                    binding.editTextVehicleModel.setText(vehicle.model)
                    binding.editTextVehicleBrand.setText(vehicle.brand)
                    binding.editTextVehicleYear.setText(vehicle.year?.toString())
                    binding.editTextFuelType.setText(vehicle.fuelType)
                    binding.editTextPrice.setText(vehicle.price?.toString())
                    binding.editTextCondition.setText(vehicle.condition)
                    binding.editTextKm.setText(vehicle.km?.toString())
                    binding.editTextPlate.setText(vehicle.plate)
                    binding.editTextDescription.setText(vehicle.description)
                    binding.editTextType.setText(vehicle.type)
                }
            }
            .addOnFailureListener { exception ->
                showMenssage("Erro ao carregar dados do veículo: ${exception.message}")
            }
    }
    
    private fun initializeEventClick() {
        val vehicleId = intent.getStringExtra("vehicleId")
        binding.btnNext.setOnClickListener {
            if (vehicleId != null) {
                // Atualiza os dados do veículo existente
                updateVehicle(vehicleId)
            } else {
                // Adiciona um novo veículo caso o vehicleId seja nulo
                createNewVehicle()
            }
        }
        
        binding.btnCancel.setOnClickListener {
            startActivity(Intent(this, VehicleActivity::class.java))
        }
    }
    
    private fun updateVehicle(vehicleId: String) {
        val updatedVehicle = Vehicle(
            vehicleId = vehicleId,
            userUID = firebaseAuth.currentUser?.uid ?: "",
            model = binding.editTextVehicleModel.text.toString(),
            brand = binding.editTextVehicleBrand.text.toString(),
            year = binding.editTextVehicleYear.text.toString().toIntOrNull(),
            fuelType = binding.editTextFuelType.text.toString(),
            price = binding.editTextPrice.text.toString().toDoubleOrNull(),
            condition = binding.editTextCondition.text.toString(),
            km = binding.editTextKm.text.toString().toIntOrNull(),
            plate = binding.editTextPlate.text.toString(),
            description = binding.editTextDescription.text.toString(),
            type = binding.editTextType.text.toString()
        )
        
        firestore.collection("vehicle")
            .document(vehicleId)
            .set(updatedVehicle)
            .addOnSuccessListener {
                showMenssage("Veículo atualizado com sucesso.")
                startActivity(Intent(this, VehicleActivity::class.java))
                finish()
            }
            .addOnFailureListener { exception ->
                showMenssage("Erro ao atualizar veículo: ${exception.message}")
            }
    }
    
    private fun createNewVehicle() {
        val model = binding.editTextVehicleModel.text.toString()
        val brand = binding.editTextVehicleBrand.text.toString()
        val year = binding.editTextVehicleYear.text.toString().toIntOrNull() ?: 0
        val fuelType = binding.editTextFuelType.text.toString()
        val price = binding.editTextPrice.text.toString().toDoubleOrNull() ?: 0.0
        val condition = binding.editTextCondition.text.toString()
        val km = binding.editTextKm.text.toString().toIntOrNull() ?: 0
        val plate = binding.editTextPlate.text.toString()
        val description = binding.editTextDescription.text.toString()
        val type = binding.editTextType.text.toString()
        val userUID = firebaseAuth.currentUser?.uid ?: run {
            showMenssage("Erro ao obter UID do usuário.")
            return
        }
        
        val vehicle = Vehicle(
            userUID = userUID,
            model = model,
            brand = brand,
            year = year,
            fuelType = fuelType,
            price = price,
            condition = condition,
            km = km,
            plate = plate,
            description = description,
            type = type
        )
        
        firestore.collection("vehicle")
            .add(vehicle)
            .addOnSuccessListener { documentReference ->
                val vehicleId = documentReference.id
                firestore.collection("vehicle").document(vehicleId).update("vehicleId", vehicleId)
                    .addOnSuccessListener {
                        showMenssage("Veículo criado com sucesso. ID: $vehicleId")
                        startActivity(Intent(this, PhotosVehicleActivity::class.java).apply {
                            putExtra("vehicleId", vehicleId)
                        })
                    }
                    .addOnFailureListener { exception ->
                        showMenssage("Erro ao atualizar veículo: ${exception.message}")
                    }
            }
            .addOnFailureListener { exception ->
                showMenssage("Erro ao adicionar veículo: ${exception.message}")
            }
    }
}
