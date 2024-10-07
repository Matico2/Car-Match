package com.example.carmatch.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.carmatch.utils.showMenssage
import com.example.carmatch1.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


class LoginActivity : AppCompatActivity() {
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    
    private lateinit var email: String
    private lateinit var password: String
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializeEventClick()
        firebaseAuth.signOut()
        
    }
    
    override fun onStart() {
        super.onStart()
        verifyUserLogged()
    }
    
    private fun verifyUserLogged() {
        val userLogged = firebaseAuth.currentUser
        if (userLogged != null) {
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }
    
    private fun initializeEventClick() {
        binding.textViewRegister.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }
        binding.buttonLogin.setOnClickListener {
            // Inicializa email e senha antes de validar os campos
            email = binding.textInputEmail.editText?.text.toString()
            password = binding.textInputPassword.editText?.text.toString()
            
            if (validateFields()) {
                loginUser()
            }
        }
    }
    
    private fun loginUser() {
        firebaseAuth.signInWithEmailAndPassword(
            email, password
        ).addOnSuccessListener {
            showMenssage("Logado com sucesso!")
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }.addOnFailureListener { erro ->
            try {
                throw erro
            } catch (erroUserInvalid: FirebaseAuthInvalidUserException) {
                showMenssage("E-mail não cadastrado")
                erroUserInvalid.printStackTrace()
            } catch (erroCredntialsUser: FirebaseAuthInvalidCredentialsException) {
                erroCredntialsUser.printStackTrace()
                showMenssage("E-mail ou senha incorretos!")
            }
        }
    }
    
    private fun validateFields(): Boolean {
        var isValid = true
        fun validate(field: String, inputLayout: TextInputLayout, errorMsg: String) {
            if (field.isEmpty()) {
                inputLayout.error = errorMsg
                isValid = false
            } else {
                inputLayout.error = null
            }
        }
        validate(email, binding.textInputEmail, "Preencha seu email!")
        validate(password, binding.textInputPassword, "Preencha sua senha!")
        return isValid
    }
}
