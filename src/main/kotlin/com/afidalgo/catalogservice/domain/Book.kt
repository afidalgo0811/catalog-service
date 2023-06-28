package com.afidalgo.catalogservice.domain

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive

data class Book(
    @NotBlank(message = "The book ISBN must be defined.")
    @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "The ISBN format must be valid")
    val isbn: String,
    @NotBlank(message = "The book title must be defined") val title: String,
    @NotBlank(message = "The book author must be defined") val author: String,
    @NotNull(message = "The book price must be defined")
    @Positive(message = "The book price must be greater than zero.")
    val price: Double,
)
