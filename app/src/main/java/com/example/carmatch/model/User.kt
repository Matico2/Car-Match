package com.example.carmatch.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    var idUser: String= "",
    var name: String= "",
    var email: String= "",
    var city: String= "",
    var cpf: String= "",
    var date: String= "",
    var password: String= "",
    var photoUser:String = ""

): Parcelable
