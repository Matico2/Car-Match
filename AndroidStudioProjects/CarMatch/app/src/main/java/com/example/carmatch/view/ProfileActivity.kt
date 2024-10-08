package com.example.carmatch.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.carmatch1.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {
    
    private val curentUser: FirebaseUser?        get() = firebaseAuth.currentUser
    private val Username = curentUser?.displayName
    
    private val firebaseAuth by lazy {
        
        FirebaseAuth.getInstance()
    }
    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val binding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializeToolbar()
        
        }
    
    private fun initializeToolbar() {
        val toolbar = binding.includeMainToolbar.toolbarApp
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = Username
            setDisplayHomeAsUpEnabled(true)
        }
    }
}