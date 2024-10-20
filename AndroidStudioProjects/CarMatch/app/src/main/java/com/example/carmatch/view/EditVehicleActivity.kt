package com.example.carmatch.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.carmatch.model.Vehicle
import com.example.carmatch.utils.showMenssage
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ActivityEditVehicleBinding
import com.example.carmatch1.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class EditVehicleActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityEditVehicleBinding.inflate(layoutInflater)
        
    }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val storage by lazy { FirebaseStorage.getInstance() }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        includeToolbarApp()
        initializeEventClick()
    }
    
    private fun includeToolbarApp() {
        val toolbar = binding.includeToolbarApp.toolbarApp
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Salvar veículo"
            setDisplayHomeAsUpEnabled(true)
        }
    }
    
    private fun initializeEventClick() {
        binding.btnNext.setOnClickListener {
            // Capturando dados dos campos de entrada
            val model = binding.editTextVehicleModel.text.toString()
            val brand = binding.editTextVehicleBrand.text.toString()
            val yearString = binding.editTextVehicleYear.text.toString()
            val fuelType = binding.editTextFuelType.text.toString()
            val priceString = binding.editTextPrice.text.toString()
            val condition = binding.editTextCondition.text.toString()
            val kmString = binding.editTextKm.text.toString()
            val plate = binding.editTextPlate.text.toString()
            val description = binding.editTextDescription.text.toString()
            val type = binding.editTextType.text.toString()
            
            // Validação de campos
            if (model.isEmpty() || brand.isEmpty() || yearString.isEmpty() || fuelType.isEmpty() ||
                priceString.isEmpty() || condition.isEmpty() || kmString.isEmpty() || plate.isEmpty() ||
                description.isEmpty() || type.isEmpty()
            ) {
                showMenssage("Por favor, preencha todos os campos.")
                return@setOnClickListener
            }
            
            // Convertendo ano, preço e quilometragem para tipos adequados
            val year = yearString.toIntOrNull() ?: 0
            val price = priceString.toDoubleOrNull() ?: 0.0
            val km = kmString.toIntOrNull() ?: 0
            
            // Criando uma instância do veículo
            val vehicle = Vehicle(
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
            // Adicionando o veículo ao Firestore
            firestore.collection("vehicle")
                .add(vehicle)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, PhotosVehicleActivity::class.java))
                    } else {
                        showMenssage("Erro ao adicionar veículo: ${task.exception?.message}")
                    }
                }
        }
    }
    
}
    
