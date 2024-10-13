package com.example.carmatch.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import com.example.carmatch.adapters.ViewPagerAdapter
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
        
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        includeToolbarApp()
        initializeNavTabs()
    }
    
    private fun initializeNavTabs() {
        
        val tabLayout = binding.tabLayout
        val viewPage = binding.viewPage
        
        val tabs = listOf("Anúncios", "Favoritos", "Conversas")
        viewPage.adapter = ViewPagerAdapter(
            tabs, supportFragmentManager, lifecycle
        )
        tabLayout.isTabIndicatorFullWidth =  true
        TabLayoutMediator(tabLayout, viewPage){ tab, position ->
            tab.text = tabs[position]
        }.attach()
    }
    
    private fun includeToolbarApp() {
        
        val toolbar = binding.includeMainToolbar.toolbarApp
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "CAR MATCH"
        }
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main, menu)
            }
            
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.item_Profile -> {
                        startActivity(
                            Intent(applicationContext, ProfileActivity::class.java)
                        )
                    }
                    
                    R.id.item_Vehicle -> {
                        startActivity(
                            Intent(applicationContext, VehicleActivity::class.java)
                        )
                    }
                }
                return true
            }
            
        })
    }
    
}