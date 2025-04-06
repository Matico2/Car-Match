package com.example.carmatch.view.activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carmatch.adapters.MensagemAdapter
import com.example.carmatch.model.Chat
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
            dataDest = extras.getParcelable<User>("dadosDestinatarios")
            
            if (idChat.isNullOrBlank()) {
                showMenssage("Erro ao abrir o chat. ID do chat inválido.")
                finish()
                return
            }
            
            if (dataDest == null || dataDest?.idUser.isNullOrBlank()) {
                showMenssage("Erro ao identificar o destinatário.")
                finish()
                return
            }
            
            val userName = dataDest?.name ?: "Nome não encontrado"
            binding.txtUserName.text = userName
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
        
        val currentUserId = firebaseAuth.currentUser?.uid ?: run {
            Log.e("MessageActivity", "Erro ao obter o ID do usuário atual.")
            showMenssage("Erro ao identificar o remetente.")
            return
        }
        
        if (idChat.isNullOrBlank()) {
            Log.e("MessageActivity", "Chat ID está vazio ou inválido.")
            showMenssage("Erro ao identificar o chat.")
            return
        }
        
        firestore.collection("Chat").document(idChat!!)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val chat = document.toObject(Chat::class.java)
                    
                    val receiverId = if (chat?.idUser1 == currentUserId) {
                        chat.idUser2  // O outro usuário
                    } else {
                        chat?.idUser1 ?: run {
                            Log.e("MessageActivity", "Erro ao identificar o destinatário.")
                            showMenssage("Erro ao identificar o destinatário.")
                            return@addOnSuccessListener
                        }
                    }
                    
                    // Criar objeto Menssage com os IDs corretos
                    val mensagem = Menssage(
                        senderId = currentUserId,
                        receiverId = receiverId,
                        text = textMessage,
                        idChat = idChat!!,
                        timestamp = null
                    )
                    
                    Log.d("MessageActivity", "Mensagem a ser enviada: $mensagem")
                    
                    // Enviar a mensagem para o Firestore
                    firestore.collection(Constants.BD_MENSANS)
                        .document(idChat!!)
                        .collection("mensagens")
                        .add(mensagem)
                        .addOnSuccessListener {
                            binding.txtMenssage.setText("") // Limpar o campo de texto
                            Log.d("MessageActivity", "Mensagem enviada com sucesso.")
                        }
                        .addOnFailureListener { exception ->
                            showMenssage("Erro ao enviar mensagem: ${exception.message}")
                            Log.e("MessageActivity", "Erro ao enviar mensagem", exception)
                        }
                } else {
                    Log.e("MessageActivity", "Chat não encontrado com ID: $idChat")
                    showMenssage("Chat não encontrado. Tente novamente.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("MessageActivity", "Erro ao acessar o chat: ${exception.message}")
                showMenssage("Erro ao identificar o chat.")
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
