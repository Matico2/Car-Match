package com.example.carmatch.model

data class Message(
    var idMessage: String = "",
    var text: String = "",
    var senderId: String = "",
    var senderName: String = "",
    var timestamp: Long = System.currentTimeMillis()
)

