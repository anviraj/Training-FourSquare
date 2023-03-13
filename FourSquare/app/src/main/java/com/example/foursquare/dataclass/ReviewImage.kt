package com.example.foursquare.dataclass

data class ReviewImage(
    val _id: String,
    val image: List<String>,
    val reviewBy: String,
    val reviewDate: String,
    val reviewerId: String,
    val reviewerImage: String
)