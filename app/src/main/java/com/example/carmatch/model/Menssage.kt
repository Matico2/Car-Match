package com.example.carmatch.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Menssage(
    var id: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val idChat  : String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)
