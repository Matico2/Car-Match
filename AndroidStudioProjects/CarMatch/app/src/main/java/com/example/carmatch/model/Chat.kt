package com.example.carmatch.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat (
    var idChat: String = "",
    var idUser1: String = "",
    var idUser2: String = "",
    var idVehicle: String = "",
    var idAd: String = "",
    var status: Boolean = true,
    var participants: List<String> = listOf()
): Parcelable
