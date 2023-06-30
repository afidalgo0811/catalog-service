package com.afidalgo.catalogservice.domain

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive

data class Book(
    @field:NotBlank(message = "The book ISBN must be defined.")
    @field:Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "The ISBN format must be valid.")
    val isbn: String,
    @field:NotBlank(message = "The book title must be defined.") val title: String,
    @field:NotBlank(message = "The book author must be defined.") val author: String,
    @field:NotNull(message = "The book price must be defined.")
    @field:Positive(message = "The book price must be greater than zero.")
    val price: Double,
)
