package com.example.carmatch.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ActivityVehicleBinding
import com.google.firebase.firestore.FirebaseFirestore

class VehicleActivity : AppCompatActivity() {
    
    
    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }
    
    private val binding by lazy {
        ActivityVehicleBinding.inflate(layoutInflater)
        
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        includeToolbarApp()
        }
    private fun includeToolbarApp() {
        val toolbar = binding.includeMainToolbar.toolbarApp
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Seus Veículos"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}