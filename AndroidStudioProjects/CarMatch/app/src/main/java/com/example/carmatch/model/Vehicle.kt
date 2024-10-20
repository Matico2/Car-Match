package com.example.carmatch.model

data class Vehicle(
     val model: String,
     val brand: String,
     val year: Int,
     val fuelType: String,
     val price: Double,
     val condition: String,
     val km: Int,
     val plate: String,
     val description: String? = null,
     val images: String? = null,
     val type: String
)
