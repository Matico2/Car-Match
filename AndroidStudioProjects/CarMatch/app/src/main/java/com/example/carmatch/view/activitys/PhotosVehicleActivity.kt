package com.example.carmatch.view.activitys

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.carmatch.utils.showMenssage
import com.example.carmatch1.databinding.ActivityPhotosVehicleBinding
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import android.widget.GridLayout
import android.widget.ImageView

class PhotosVehicleActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPhotosVehicleBinding.inflate(layoutInflater) }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val storage by lazy { FirebaseStorage.getInstance() }
    private var cameraPermission = false
    private var galeryPermission = false
    private val photoUris = mutableListOf<Uri>()
    private var vehicleId: String? = null
    
    companion object {
        private const val GALLERY_REQUEST_CODE = 1001
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        includeToolbarApp()
        permissions()
        
        // Obtém o vehicleId passado pela EditVehicleActivity
        vehicleId = intent.getStringExtra("vehicleId")
        
        binding.btnAddPhoto.setOnClickListener {
            openGallery()
        }
        
        binding.btnSave.setOnClickListener {
            savePhotos()
        }
    }
    
    private fun includeToolbarApp() {
        val toolbar = binding.includeToolbarApp.toolbarApp
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Adicione fotos do veículo"
            setDisplayHomeAsUpEnabled(true)
        }
    }
    
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.let { intent ->
                if (intent.clipData != null) {
                    val count = intent.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = intent.clipData!!.getItemAt(i).uri
                        photoUris.add(imageUri)
                    }
                } else {
                    intent.data?.let { imageUri ->
                        photoUris.add(imageUri)
                    }
                }
            }
            displaySelectedPhotos()
        }
    }
    
    private fun displaySelectedPhotos() {
        val photoContainer = binding.photoContainer
        photoContainer.removeAllViews()
        
        photoUris.forEach { uri ->
            val imageView = ImageView(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                }
                setImageURI(uri)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
            photoContainer.addView(imageView)
        }
    }
    
    private fun savePhotos() {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null && vehicleId != null) {
            val uploadTasks = mutableListOf<Task<Uri>>()
            
            photoUris.forEachIndexed { index, uri ->
                val fileRef = storage.reference.child("photos/vehicles/$userId/$vehicleId/photo_$index.jpg")
                uploadTasks.add(fileRef.putFile(uri).continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw task.exception ?: Exception("Upload failed")
                    }
                    fileRef.downloadUrl
                })
            }
            
            Tasks.whenAllSuccess<Uri>(uploadTasks).addOnSuccessListener { downloadUris ->
                showMenssage("Fotos salvas com sucesso!")
                startActivity(Intent(this, VehicleActivity::class.java))
            }.addOnFailureListener {
                showMenssage("Falha ao salvar as fotos!")
            }
        } else {
            showMenssage("Erro: ID do usuário ou do veículo não encontrado.")
        }
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
}
