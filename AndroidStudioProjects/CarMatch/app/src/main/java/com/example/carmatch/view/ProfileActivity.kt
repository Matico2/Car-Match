package com.example.carmatch.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import com.example.carmatch.utils.showMenssage
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ActivityProfileBinding
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {
    
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val storage by lazy { FirebaseStorage.getInstance() }
    private val binding by lazy { ActivityProfileBinding.inflate(layoutInflater) }
    
    private var cameraPermission = false
    private var galeryPermission = false
    // Gerenciador para selecionar a imagem da galeria
    private val galeryMenage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.imgProfilePhoto.setImageURI(uri)
                uploadImageStorage(uri)
            } else {
                showMenssage("Nenhuma imagem selecionada :?")
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        includeToolbarApp()
        permissions()
        initializeEventClick()
        recoverData()
    }
    private fun recoverData() {
        val idUser = firebaseAuth.currentUser?.uid
        if(idUser != null) {
            firestore.collection("users")
                .document(idUser)
                .get()
                .addOnSuccessListener {documentSnapShot ->
                    val dateUser = documentSnapShot.data
                    if(dateUser != null){
                        val name = dateUser["name"] as String
                        val photoUser = dateUser ["photoUser"] as String
                        val email = dateUser["email"] as String
                        binding.txtUsername.setText(name)
                        binding.txtEmail.setText(email)
                        if (photoUser.isNotEmpty()){
                            Picasso.get()
                                .load(photoUser)
                                .into(binding.imgProfilePhoto)
                        }
                    }
                    
                }
        }
    }
    
    private fun uploadImageStorage(uri: Uri) {
        val idUser = firebaseAuth.currentUser?.uid
        if (idUser != null) {
            storage.getReference("photos").child("users").child(idUser).child("perfil.jpg")
                .putFile(uri)
                .addOnSuccessListener { task ->
                    showMenssage("Foto de perfil sava com sucesso!")
                    task.metadata?.reference?.downloadUrl?.addOnSuccessListener { uir ->
                        val data = mapOf(
                            "photoUser" to uri.toString()
                        )
                        updateData(idUser,data)
                    }
                }
                .addOnFailureListener { showMenssage("Falha ao salvar sua foto de perfil!") }
        }
    }
    
    private fun updateData(idUser: String, date: Map<String, String>) {
        firestore.collection("users").document(idUser).update(date)
            .addOnSuccessListener { showMenssage("Sucesso ao atualizar o perfil do usuário.")}
            .addOnFailureListener{showMenssage("Erro ao atualizar os dados do usuário.")}
    }
    
    
    
    // Função para solicitar permissões
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
        val toolbar = binding.includeToolbarApp.toolbarApp
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Seu Perfil"
            setDisplayHomeAsUpEnabled(true)
        }
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.profile_menu, menu)
            }
            
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_share_profile -> shareProfile()
                    R.id.action_logout -> logout()
                }
                return true
            }
        })
    }
    
    // mostrando e-mail do usuário cadastrado
    
    private fun initializeEventClick() {
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
        binding.txtChangePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
        // Evento de clique para adicionar foto de perfil
        binding.btnAddPhoto.setOnClickListener {
            if (galeryPermission) {
                galeryMenage.launch("image/*")
            } else {
                showMenssage("Não possui permissão :/")
                permissions()
            }
        }
        
        binding.txtDeleteAccount.setOnClickListener {
            val user = Firebase.auth.currentUser
            if (user != null) {
                deleteAccount(user.uid)
            }
        }
    }
    
    private fun deleteAccount(userId: String) {
        val user = Firebase.auth.currentUser
        if (user != null) {
            // Exibir um diálogo de confirmação
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmação de Exclusão")
            builder.setMessage("Você tem certeza que deseja deletar sua conta? Essa ação não pode ser desfeita.")
            builder.setPositiveButton("Deletar") { dialog, _ ->
                
                // Referência ao documento do usuário no Firestore
                val userDocRef = firestore.collection("users").document(userId)
                
                // Deletar dados do Firestore
                userDocRef.delete()
                    .addOnCompleteListener { firestoreTask ->
                        if (firestoreTask.isSuccessful) {
                            // Após deletar os dados do Firestore, deletar a conta do usuário no Auth
                            user.delete()
                                .addOnCompleteListener { authTask ->
                                    if (authTask.isSuccessful) {
                                        showMenssage("Usuário deletado com sucesso")
                                        startActivity(
                                            Intent(
                                                applicationContext,
                                                LoginActivity::class.java
                                            )
                                        )
                                        finish()
                                    }
                                }
                        } else {
                            showMenssage("Falha ao deletar dados do Firestore: ${firestoreTask.exception?.message}")
                        }
                    }
                
                // Deletar imagens ou outros dados no Storage (se necessário)
                val storageRef = Firebase.storage.reference.child("images/$userId/")
                storageRef.listAll()
                    .addOnSuccessListener { listResult ->
                        listResult.items.forEach { item ->
                            item.delete().addOnSuccessListener {
                                // Sucesso ao deletar cada arquivo de storage
                            }.addOnFailureListener {
                                showMenssage("Falha ao deletar arquivo de storage: ${it.message}")
                            }
                        }
                    }.addOnFailureListener {
                        showMenssage("Falha ao listar arquivos do Storage: ${it.message}")
                    }
                
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
    
    
    private fun logout() {
        AlertDialog.Builder(this)
            .setTitle("Deslogar")
            .setMessage("Deseja realmente sair?")
            .setNegativeButton("Não", null)
            .setPositiveButton("Sim") { _, _ ->
                firebaseAuth.signOut()
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
            .create()
            .show()
    }
    
    private fun shareProfile() {
        // TODO: Implementar compartilhamento de perfil
    }
}