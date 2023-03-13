package com.example.foursquare.dataclass

data class GetSearchPlacesRequest(
    val latitude: Double,
    val longitude: Double,
    val text: String
)