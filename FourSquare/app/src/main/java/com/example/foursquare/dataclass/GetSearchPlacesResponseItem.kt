package com.example.foursquare.dataclass

data class GetSearchPlacesResponseItem(
    val _id: String,
    val address: String,
    val category: String,
    val city: String,
    val distance: DistanceX,
    val location: LocationX,
    val placeImage: String,
    val placeName: String,
    val priceRange: Int,
    val rating: Double
)