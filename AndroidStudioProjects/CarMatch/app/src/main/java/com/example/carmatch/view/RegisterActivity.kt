package com.example.carmatch.view

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.example.carmatch.utils.showMenssage
import com.example.carmatch1.databinding.ActivityRegisterBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore


class RegisterActivity : AppCompatActivity() {
    
    // campos tabela
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var city: String
    private lateinit var cpf: String
    private lateinit var date: String
    private lateinit var password: String
    
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }
    
    
    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        
        initializeToolbar()
        initializeEventClick()
    }
    
    private fun initializeEventClick() {
        binding.buttonRegister.setOnClickListener {
            // Pegando os valores dos campos
            name = binding.textInputName.editText?.text.toString()
            email = binding.textInputEmail.editText?.text.toString()
            city = binding.textInputCity.editText?.text.toString()
            cpf = binding.textInputCPF.editText?.text.toString()
            date = binding.textInputDate.editText?.text.toString()
            password = binding.textInputPassword.editText?.text.toString()
            
            if (this.validateFields()) {
                registerUSer(name, email, city, cpf, date, password)
            } else {
            
            }
        }
    }
    
    private fun registerUSer(
        name: String,
        email: String,
        city: String,
        cpf: String,
        date: String,
        password: String
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    val idUser = result.result.user?.uid
                    if(idUser != null){
                        val user = com.example.carmatch.model.User(
                            idUser, name, email,city, cpf, date, password
                        )
                        saveUserFirestore(user)
                        
                        
                    }
                    
                }
                
            }.addOnFailureListener { erro ->
                try {
                    throw erro
                } catch (errorWeakPassword: FirebaseAuthWeakPasswordException) {
                    showMenssage("Sua senha está fraca. Por favor, digite uma senha com letras, números e caracteres especiais.")
                    errorWeakPassword.printStackTrace()
                }  catch (errorEmailConflict: FirebaseAuthUserCollisionException) {
                    showMenssage("E-mail ja cadastrado.")
                    errorEmailConflict.printStackTrace()
                }catch (errorIvalidCredentials: FirebaseAuthInvalidCredentialsException) {
                    showMenssage("E-mail inválido, digite outro e-mail.")
                    errorIvalidCredentials.printStackTrace()
                }
                
            }
    }
    
    private fun saveUserFirestore(user: com.example.carmatch.model.User) {
        firestore
            .collection("users")
            .document(user.idUser)
            .set(user)
            .addOnSuccessListener {
                showMenssage("Sucesso ao realizar o seu cadastro :)")
                startActivity(
                    Intent(applicationContext, MainActivity::class.java)
                
                )
            }.addOnFailureListener{
                showMenssage("Erro ao fazer seu cadastro :(")
            }
    }
    
    // Validação dos campos
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
        validate(name, binding.textInputName, "Preencha seu nome!")
        validate(email, binding.textInputEmail, "Preencha seu email!")
        validate(city, binding.textInputCity, "Preencha sua cidade!")
        validate(cpf, binding.textInputCPF, "Preencha seu CPF!")
        validate(date, binding.textInputDate, "Preencha sua data de nascimento!")
        validate(password, binding.textInputPassword, "Preencha sua senha!")
        
        return isValid
    }
    
    // Inicializa a toolbar e aplica o botão de voltar
    private fun initializeToolbar() {
        val toolbar = binding.includeToolbarApp.toolbarApp
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Faça seu cadastro"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}
