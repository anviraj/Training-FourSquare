package com.example.foursquare.dataclass

data class GetFavouritesRequest(
    val latitude: Double,
    val longitude: Double,
    val text: String
)