package com.example.carmatch.view.activitys

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import com.example.carmatch.view.fragments.AdVehiclesFragment
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        includeToolbarApp()
        showAdVehicleFragment()
    }
    
    private fun includeToolbarApp() {
        val toolbar = binding.includeMainToolbar.toolbarApp
        setSupportActionBar(toolbar)
        supportActionBar?.title = "CAR MATCH"
        
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main, menu)
            }
            
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.item_Profile -> {
                        startActivity(Intent(applicationContext, ProfileActivity::class.java))
                        return true
                    }
                    R.id.item_Vehicle -> {
                        startActivity(Intent(applicationContext, VehicleActivity::class.java))
                        return true
                    }
                    R.id.item_Chat -> {
                        startActivity(Intent(applicationContext, ChatActivity::class.java))
                        return true
                    }
                    R.id.item_Fav -> {
                        startActivity(Intent(applicationContext, FavVehicleActivity::class.java))
                        return true
                    }
                }
                return false
            }
        })
    }
    
    private fun showAdVehicleFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, AdVehiclesFragment())
            .commitAllowingStateLoss()
    }
}

