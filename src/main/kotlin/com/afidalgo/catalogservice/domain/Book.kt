package com.afidalgo.catalogservice.domain

data class Book(
    val isbn: String,
    val title: String,
    val author: String,
    val price: Double,
)
