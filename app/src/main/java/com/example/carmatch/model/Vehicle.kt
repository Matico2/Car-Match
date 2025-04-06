package com.example.carmatch.model

data class Vehicle(
     val vehicleId: String = "",
     val model: String= "",
     val brand: String= "",
     val year: Int? = null,
     val fuelType: String = "",
     val price: Double? = null,
     val condition: String = "",
     val km: Int? = null,
     val plate: String = "",
     val description: String = "",
     val images: String = "",
     val type: String = "",
     val userUID: String = ""
)
