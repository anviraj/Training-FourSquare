package com.example.foursquare.dataclass

data class GetParticularPlaceResponse(
    val _id: String,
    val address: String,
    val category: String,
    val city: String,
    val location: Location,
    val overview: String,
    val placeImage: String,
    val placeName: String,
    val placePhone: String,
    val priceRange: Int,
    val rating: Float
)