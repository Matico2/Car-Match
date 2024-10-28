package com.example.carmatch.view


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.carmatch.view.fragments.VehicleListFragment
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ActivityVehicleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class VehicleActivity : AppCompatActivity() {
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val storage by lazy { FirebaseStorage.getInstance() }
    private val binding by lazy { ActivityVehicleBinding.inflate(layoutInflater) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        includeToolbarApp()
        initializeEventClick()
        inicializeFrag()
    }
    
    private fun includeToolbarApp() {
        val toolbar = binding.includeMainToolbar.toolbarApp
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Seus Veículos"
            setDisplayHomeAsUpEnabled(true)
        }
    }
    
    private fun initializeEventClick() {
        binding.btnAddVehicle.setOnClickListener {
            startActivity(Intent(this, EditVehicleActivity::class.java))
        }
        binding.fragmentContainer.setOnClickListener{
            startActivity(Intent(this, DetailsVehicleActivity::class.java))
        }
    }
    
    private fun inicializeFrag() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, VehicleListFragment())
            .commit()
    }
}
