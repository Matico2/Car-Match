package com.example.carmatch.view.activitys


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.carmatch.view.fragments.VehicleListFragment
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ActivityVehicleBinding

class VehicleActivity : AppCompatActivity() {
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
        binding.fragmentContainerAd.setOnClickListener{
            startActivity(Intent(this, DetailsVehicleActivity::class.java))
        }
    }
    
    private fun inicializeFrag() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_ad, VehicleListFragment())
            .commit()
    }
}
