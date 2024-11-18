package com.example.carmatch.view.activitys

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.carmatch.model.Chat
import com.example.carmatch.model.User
import com.example.carmatch.utils.Constants
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ActivityMessageBinding
import com.squareup.picasso.Picasso

class MessageActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMessageBinding.inflate(layoutInflater) }
    private var dataDest: User? = null
    private var userName: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        recoverData()
        includeToolbarApp()
    }
    
    private fun includeToolbarApp() {
        val toolbar = binding.tb
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            // Exibir os dados do usuário
            userName?.let {
                binding.txtUserName.setText(it) // Nome do usuário
            }
            
            dataDest?.photoUser?.let { photoUrl ->
                //Picasso.get()
                //.load(photoUrl)
                //.placeholder(R.drawable.placeholder_image) // Adicione uma imagem placeholder
                // .error(R.drawable.error_image) // Adicione uma imagem de erro
                // .into(binding.imagePhoto)
            }
        }
    }
    
    private fun recoverData() {
        val extras = intent.extras
        if (extras != null) {
            val origem = extras.getString("origem")
            if (origem == Constants.ORIGEM_CONTATO) {
                // Recuperar nome do usuário enviado
                userName = extras.getString("userName", "Usuário desconhecido")
                // Recuperar o objeto Chat enviado
                val chat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    extras.getParcelable("dadosDestinatarios", Chat::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    extras.getParcelable("dadosDestinatarios")
                }
                
            
                if (chat != null) {
                    //binding.editTextMessage.setText(chat.idChat)
                }
            }
        }
        
    }
}
