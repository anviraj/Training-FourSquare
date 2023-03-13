package com.example.foursquare.dataclass

data class GetFilterSearchResponseItem(
    val _id: String,
    val address: String,
    val category: String,
    val city: String,
    val distance: DistanceXX,
    val features: List<FeatureX>,
    val location: LocationXX,
    val placeImage: String,
    val placeName: String,
    val popularityCount: Int,
    val priceRange: Int,
    val rating: Double
)