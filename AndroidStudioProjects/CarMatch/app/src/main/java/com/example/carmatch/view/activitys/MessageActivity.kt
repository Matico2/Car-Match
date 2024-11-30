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
    private var idChat: String? = null
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
            
            if (idChat.isNullOrBlank()) {
                showMenssage("Erro ao abrir o chat. ID do chat inválido.")
                finish()
                return
            }
            
            val userName = dataDest?.name ?: "Nome não encontrado"
            val vehicleModel = extras.getString("vehicleModel")
            binding.txtUserName.text = "$userName"
        } ?: run {
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
        val messagesListener = chatMessagesRef
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Listener Error", "Erro ao recuperar mensagens: ${error.message}")
                    return@addSnapshotListener
                }
                
                if (snapshot == null || snapshot.isEmpty) {
                    Log.d("Firestore", "Nenhuma mensagem encontrada para o chat ID: $idChat")
                    updateMessages(emptyList())
                    return@addSnapshotListener
                }
                
                val messages = snapshot.documents.mapNotNull { document ->
                    val mensagem = document.toObject(Menssage::class.java)
                    mensagem?.apply { id = document.id }
                }
                
                Log.d("Firestore", "Mensagens carregadas: ${messages.size}")
                updateMessages(messages)
            }
        
        listenerRegistrations.add(messagesListener)
    }
    
    private fun updateMessages(newMessages: List<Menssage>) {
        Log.d("MessageActivity", "Atualizando RecyclerView com ${newMessages.size} mensagens.")
        newMessages.forEach { mensagem ->
            Log.d(
                "MessageActivity",
                "Mensagem: Text=${mensagem.text}, SenderId=${mensagem.senderId}, ReceiverId=${mensagem.receiverId}"
            )
        }
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
        
        val currentUserId = firebaseAuth.currentUser?.uid ?: return
        val receiverId = dataDest?.idUser.orEmpty()
        
        if (receiverId.isBlank()) {
            Log.e("MessageActivity", "ReceiverId está vazio.")
            showMenssage("Erro ao identificar o destinatário.")
            return
        }
        
        val mensagem = Menssage(
            senderId = currentUserId,
            receiverId = receiverId,
            text = textMessage,
            idChat = idChat.orEmpty(),
            timestamp = null // Será preenchido automaticamente no Firestore
        )
        
        Log.d("MessageActivity", "Enviando mensagem: $mensagem")
        
        firestore.collection(Constants.BD_MENSANS)
            .document(idChat!!).collection("mensagens")
            .add(mensagem)
            .addOnSuccessListener {
                binding.txtMenssage.setText("")
                Log.d("MessageActivity", "Mensagem enviada com sucesso.")
            }
            .addOnFailureListener {
                showMenssage("Erro ao enviar mensagem: ${it.message}")
                Log.e("MessageActivity", "Erro ao enviar mensagem", it)
            }
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
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        listenerRegistrations.forEach { it.remove() }
    }
}
