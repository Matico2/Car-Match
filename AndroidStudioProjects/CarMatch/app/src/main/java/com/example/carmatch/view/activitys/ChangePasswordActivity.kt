package com.example.carmatch.view.activitys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.carmatch.utils.showMenssage
import com.example.carmatch1.databinding.ActivityChangePasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordActivity : AppCompatActivity() {
    
    private val binding by lazy {
        ActivityChangePasswordBinding.inflate(layoutInflater)
    }
    
    private val user = FirebaseAuth.getInstance().currentUser
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        includeToolbarApp()
        initializeEventClick()
    }
    
    private fun includeToolbarApp() {
        val toolbar = binding.includeToolbarApp.toolbarApp
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Trocar senha"
            setDisplayHomeAsUpEnabled(true)
        }
    }
    
    private fun initializeEventClick() {
        binding.buttonChangePassword.setOnClickListener {
            val email = user?.email
            val currentPassword = binding.editTextCurrentPassword.text.toString()
            val newPassword = binding.editTextNewPassword.text.toString()
            
            if (email != null && currentPassword.isNotEmpty() && newPassword.isNotEmpty()) {
                val credential = EmailAuthProvider.getCredential(email, currentPassword)
                
                user?.reauthenticate(credential)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                showMenssage("Senha trocada com sucesso.")
                                startActivity(
                                    Intent(this, MainActivity::class.java)
                                )
                            } else {
                                showMenssage("Não foi possível trocar a senha.")
                            }
                        }
                    } else {
                        showMenssage("Erro, Verifique sua senha atual.")
                    }
                }
            } else {
                showMenssage("Por favor, preencha todos os campos.")
            }
        }
        binding.buttonCancel.setOnClickListener{
            startActivity(
                Intent(this, ProfileActivity::class.java)
            )
        }
    }
}
