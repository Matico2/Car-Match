package com.example.carmatch.view.activitys

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.carmatch.view.fragments.AdVehiclesFragment
import com.example.carmatch.view.fragments.FavVehicleFragment
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ActivityFavVehicleBinding

class FavVehicleActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFavVehicleBinding.inflate(layoutInflater) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        includeToolbarApp()
        showFavVehicleFragment()
    }
    
    private fun includeToolbarApp() {
        val toolbar = binding.includeMainToolbar.toolbarApp
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Favoritos"
            setDisplayHomeAsUpEnabled(true)
        }
    }
    
    private fun showFavVehicleFragment() {
        Log.d("FavVehicleActivity", "Iniciando o fragmento de favoritos")
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_fav, FavVehicleFragment())
            .commitAllowingStateLoss()
        Log.d("FavVehicleActivity", "Fragmento de favoritos adicionado")
    }
    
    
}
