package com.example.carmatch.view

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.carmatch.utils.showMenssage
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class EditProfileActivity : AppCompatActivity() {
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val binding by lazy { ActivityEditProfileBinding.inflate(layoutInflater) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        includeToolbarApp()
        loadUserData()
        
        binding.buttonSave.setOnClickListener {
            updateUserData()
        }
    }
    
    private fun includeToolbarApp() {
        val toolbar = binding.includeToolbarApp.toolbarApp
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Editar perfil"
            setDisplayHomeAsUpEnabled(true)
        }
    }
    
    // preenche as informaçoes do usuário
    private fun loadUserData() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.editTextName.setText(document.getString("name"))
                    binding.editTextCpf.setText(document.getString("cpf"))
                    binding.editTextDate.setText(document.getString("date"))
                    binding.editTextEmail.setText(document.getString("email"))
                    binding.editTextCity.setText(document.getString("city"))
                }
            }
            .addOnFailureListener { exception ->
                showMenssage("Erro ao carregar dados: ${exception.message}")
            }
    }
    // atualiza as informações do usuário
    private fun updateUserData() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val userData = hashMapOf(
            "name" to binding.editTextName.text.toString(),
            "cpf" to binding.editTextCpf.text.toString(),
            "date" to binding.editTextDate.text.toString(),
            "email" to binding.editTextEmail.text.toString(),
            "city" to binding.editTextCity.text.toString()
        )
        firestore.collection("users").document(userId)
            .update(userData as Map<String, Any>)
            .addOnSuccessListener {
                showMenssage("Dados atualizados com sucesso!")
                finish()
            }
            .addOnFailureListener { exception ->
                showMenssage("Erro ao atualizar dados: ${exception.message}")
            }
 
    }
    
}
