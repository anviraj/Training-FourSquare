package com.example.foursquare.dataclass

data class Feature(
    val _id: String,
    val acceptedCredit: Boolean,
    val delivery: Boolean,
    val dogFriendly: Boolean,
    val familyFriendly: Boolean,
    val inWalkingDistance: Boolean,
    val outdoorDining: Boolean,
    val parking: Boolean,
    val wifi: Boolean
)