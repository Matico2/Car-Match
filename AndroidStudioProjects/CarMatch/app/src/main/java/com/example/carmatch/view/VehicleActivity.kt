package com.example.carmatch.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.carmatch.utils.showMenssage
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ActivityProfileBinding
import com.example.carmatch1.databinding.ActivityVehicleBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class VehicleActivity : AppCompatActivity() {
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val storage by lazy { FirebaseStorage.getInstance() }
    private val binding by lazy { ActivityVehicleBinding.inflate(layoutInflater) }
    private var cameraPermission = false
    private var galeryPermission = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        includeToolbarApp()
        initializeEventClick()
        
        }
    
    private fun permissions() {
        cameraPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        
        galeryPermission =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
        
        val listNoPermission = mutableListOf<String>()
        if (!cameraPermission) listNoPermission.add(Manifest.permission.CAMERA)
        if (!galeryPermission) {
            listNoPermission.add(
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }
            )
        }
        
        if (listNoPermission.isNotEmpty()) {
            val permissionManager =
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                    cameraPermission = permissions[Manifest.permission.CAMERA] ?: cameraPermission
                    galeryPermission =
                        permissions[if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            Manifest.permission.READ_MEDIA_IMAGES
                        } else {
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        }] ?: galeryPermission
                }
            permissionManager.launch(listNoPermission.toTypedArray())
        }
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
        
    }
    }
