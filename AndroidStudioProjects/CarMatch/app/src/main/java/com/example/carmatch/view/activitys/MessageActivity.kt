package com.example.carmatch.view.activitys

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carmatch.adapters.MensagemAdapter
import com.example.carmatch.model.Menssage
import com.example.carmatch.model.User
import com.example.carmatch.utils.Constants
import com.example.carmatch.utils.showMenssage
import com.example.carmatch1.databinding.ActivityMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class MessageActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMessageBinding.inflate(layoutInflater) }
    private var dataDest: User? = null
    private var idChat: String? = null // Agora é opcional
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private var listenerRegistrations = mutableListOf<ListenerRegistration>()
    private lateinit var mensagemAdapter: MensagemAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        
        mensagemAdapter = MensagemAdapter { mensagem ->
            deleteMessage(
                mensagem.id,
                onSuccess = { showMenssage("Mensagem apagada com sucesso!") },
                onFailure = { showMenssage("Erro ao apagar mensagem: ${it.message}") }
            )
        }
        
        recoverData()
        includeToolbarApp()
        initRecycleView()
        initListeners()
        eventClick()
    }
    
    private fun recoverData() {
        intent.extras?.let { extras ->
            idChat = extras.getString("idChat")
            dataDest = extras.getParcelable("dadosDestinatarios")
            Log.d("RecoverData", "Intent Extras: $extras")
            Log.d("RecoverData", "idChat: $idChat, dataDest: $dataDest")
            
            if (idChat.isNullOrBlank()) {
                Log.e("RecoverData", "idChat não foi fornecido ou está inválido.")
                showMenssage("Erro ao abrir o chat. ID do chat inválido.")
                finish()
                return
            }
            Log.d("RecoverData", "idChat recuperado: $idChat")
        } ?: run {
            Log.e("RecoverData", "Extras estão nulos.")
            showMenssage("Erro ao abrir o chat. Dados insuficientes.")
            finish()
        }
    }
    
    
    private fun initRecycleView() {
        binding.recyclerViewChat.apply {
            adapter = mensagemAdapter
            layoutManager = LinearLayoutManager(this@MessageActivity)
        }
    }
    
    private fun initListeners() {
        if (idChat.isNullOrBlank()) {
            Log.e("Firestore Error", "idChat está vazio. Não é possível configurar o listener.")
            return
        }
        
        val chatMessagesRef = firestore.collection(Constants.BD_MENSANS)
            .document(idChat!!).collection("mensagens")
        
        Log.d("Firestore", "Listener configurado para idChat: $idChat")
        
        val messagesListener = chatMessagesRef
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Listener Error", "Erro ao recuperar mensagens: ${error.message}")
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents?.mapNotNull {
                    it.toObject(Menssage::class.java)?.apply { id = it.id }
                } ?: emptyList()
                updateMessages(messages)
            }
        listenerRegistrations.add(messagesListener)
    }
    
    
    private fun updateMessages(newMessages: List<Menssage>) {
        mensagemAdapter.addList(newMessages)
        if (newMessages.isNotEmpty()) {
            binding.recyclerViewChat.smoothScrollToPosition(newMessages.size - 1)
        }
    }
    
    private fun eventClick() {
        binding.btnSend.setOnClickListener {
            val textMessage = binding.txtMenssage.text.toString()
            saveMenssage(textMessage)
        }
    }
    
    private fun saveMenssage(textMessage: String) {
        if (textMessage.isBlank()) {
            showMenssage("Digite uma mensagem antes de enviar.")
            return
        }
        val idUserSend = firebaseAuth.currentUser?.uid ?: return
        val menssage = Menssage(
            senderId = idUserSend,
            receiverId = dataDest?.idUser.orEmpty(),
            text = textMessage,
            idChat = idChat.orEmpty()
        )
        saveMenssageFireStore(menssage, {
            binding.txtMenssage.setText("")
        }, {
            showMenssage("Erro ao enviar mensagem: ${it.message}")
        })
    }
    
    private fun saveMenssageFireStore(
        menssage: Menssage,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val documentRef = firestore.collection(Constants.BD_MENSANS)
            .document(idChat!!).collection("mensagens").document()
        menssage.id = documentRef.id
        
        documentRef.set(menssage)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
    
    private fun deleteMessage(
        idMessage: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection(Constants.BD_MENSANS)
            .document(idChat!!).collection("mensagens").document(idMessage)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
    
    private fun includeToolbarApp() {
        val toolbar = binding.tb
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            binding.txtUserName.text = dataDest?.name
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        listenerRegistrations.forEach { it.remove() }
    }
}
