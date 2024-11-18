package com.example.carmatch.view.activitys

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
    
    
    // função para esperar o click do usuário
    private fun initializeEventClick() {
        
        //função para o usuário ser direcionado para a tela de registro
        binding.textViewRegister.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }
        
        // Função para recuperação de senha
        binding.textViewForgotPassword.setOnClickListener {
            email = binding.textInputEmail.editText?.text.toString().trim()
            if (email.isNotEmpty()) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showMenssage("Verifique seu e-mail.")
                        } else {
                            showMenssage("E-mail inválido ou não cadastrado.")
                        }
                    }
            } else {
                showMenssage("Por favor, preencha o campo de e-mail.")
            }
        }
        
        // Função de login
        binding.buttonLogin.setOnClickListener {
            email = binding.textInputEmail.editText?.text.toString().trim()
            password = binding.textInputPassword.editText?.text.toString().trim()
            
            if (validateFields()) {
                loginUser()
            }
        }
    }
    
    private fun loginUser() {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
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
                } catch (erroCredentialsUser: FirebaseAuthInvalidCredentialsException) {
                    erroCredentialsUser.printStackTrace()
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
