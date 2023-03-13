package com.example.foursquare.dataclass

data class GetProfileResponse(
    val _id: String,
    val email: String,
    val phoneNumber: String,
    val userImage: String,
    val userName: String
)