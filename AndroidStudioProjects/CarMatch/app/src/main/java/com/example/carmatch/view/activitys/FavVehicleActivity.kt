package com.example.carmatch.view.activitys

import FavVehicleFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_fav, FavVehicleFragment())
            .commit()
    }
}