package com.example.foursquare.dataclass

data class NearYouResponseItem(
    val _id: String,
    val address: String,
    val category: String,
    val city: String,
    val distance: Distance,
    val features: List<Feature>,
    val placeImage: String,
    val placeName: String,
    val popularityCount: Int,
    val priceRange: Int,
    val rating: Double
)