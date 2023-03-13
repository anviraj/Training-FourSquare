package com.example.foursquare.dataclass

data class GetFavouritesResponseItem(
    val __v: Int,
    val _id: String,
    val distance: DistanceXXX,
    val features: List<FeatureXX>,
    val location: LocationXXX,
    val placeAddress: String,
    val placeCategory: String,
    val placeCity: String,
    val placeId: String,
    val placeImage: String,
    val placeName: String,
    val placePriceRange: Int,
    val popularityCount: Int,
    val rating: Double,
    val userEmail: String
)