package com.example.carmatch.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import com.example.carmatch1.databinding.ActivityChangePasswordBinding
import com.example.carmatch1.databinding.ActivityProfileBinding
import com.example.carmatch1.databinding.FragmentChangePasswordBinding
import com.example.carmatch1.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {
    
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    
    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }
    
    private val binding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    
    private var cameraPermission = false
    private var galeryPermission = false
    
    // Gerenciador para selecionar a imagem da galeria
    private val galeryMenage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            binding.imgProfilePhoto.setImageURI(uri)
        } else {
            showMenssage("Nenhuma imagem selecionada :?")
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        includeToolbarApp()
        initializeEventClick()
        permissions()
    }
    
    //função para solicitar permissões
    private fun permissions() {
        cameraPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        
        // Verifica se a versão do Android é >= 13 (API 33) para usar READ_MEDIA_IMAGES, ou READ_EXTERNAL_STORAGE para versões anteriores
        galeryPermission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
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
        if (!cameraPermission)
            listNoPermission.add(Manifest.permission.CAMERA)
        
        if (!galeryPermission) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                listNoPermission.add(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                listNoPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        
        if (listNoPermission.isNotEmpty()) {
            val permissionManager = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                cameraPermission = permissions[Manifest.permission.CAMERA] ?: cameraPermission
                
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    galeryPermission = permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: galeryPermission
                } else {
                    galeryPermission = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: galeryPermission
                }
            }
            permissionManager.launch(listNoPermission.toTypedArray())
        }
    }
    
    // função para configurar a toolbar
    private fun includeToolbarApp() {
        val toolbar = binding.includeToolbarApp.toolbarApp
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Seus Perfil"
            setDisplayHomeAsUpEnabled(true)
        }
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.profile_menu, menu)
            }
            
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_share_profile -> {
                        shareProfile()
                    }
                    
                    R.id.action_logout -> {
                        logout()
                    }
                }
                return true
            }
            
        })
    }
    
    // Inicializa os eventos de clique dos botões
    private fun initializeEventClick() {
        binding.btnEditProfile.setOnClickListener {
            startActivity(
                Intent(this, EditProfileActivity::class.java )
            )
        }
        binding.txtChangePassword.setOnClickListener {
            startActivity(
                Intent(this, ChangePasswordActivity::class.java)
            )
        }
        val user = firebaseAuth.currentUser
        if (user != null) {
            val userId = user.uid
            // Referência ao Firestore
            val db = FirebaseFirestore.getInstance()
            
            AlertDialog.Builder(this)
                .setTitle("Deletar Conta")
                .setMessage("Você tem certeza que deseja deletar sua conta? Essa operação não possui volta!")
                .setPositiveButton("Deletar") { _, _ ->
                    
                    // Deleta os dados do usuário do Firestore
                    db.collection("usuarios").document(userId)
                        .delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Agora, deletar a conta de autenticação
                                user.delete()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                this,
                                                "Sua conta foi deletada com sucesso!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            startActivity(Intent(this, LoginActivity::class.java))
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Erro ao deletar conta: ${task.exception?.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            Log.e(
                                                "ProfileActivity",
                                                "Erro ao deletar conta",
                                                task.exception
                                            )
                                        }
                                    }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Erro ao deletar dados: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        } else {
            Toast.makeText(this, "Você não está logado :/", Toast.LENGTH_SHORT).show()
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
    }
    
    private fun shareProfile() {
        // TODO: Implementar compartilhamento de perfil
    }
    
    private fun logout() {
        AlertDialog.Builder(this)
            .setTitle("Deslogar")
            .setMessage("Deseja realmente sair?")
            .setNegativeButton("Não") { dialog, posicao -> }
            .setPositiveButton("Sim") { dialog, posicao ->
                firebaseAuth.signOut()
                startActivity(
                    Intent(applicationContext, LoginActivity::class.java)
                )
            }
            .create()
            .show()
    }
}
