package com.example.carmatch.model

data class AdVehicle(
    var idUser: String = "",
    var idVehicle: String = "",
    var idAd: String = "",
    var status: Boolean = true,
    var model: String = "",
    var price: String = "",
    var location: String = "",
    var imageUrls: List<String> = listOf()
)
