package com.afidalgo.catalogservice.domain

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version

data class Book(
    @Id val id: Long?,
    @field:NotBlank(message = "The book ISBN must be defined.")
    @field:Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "The ISBN format must be valid.")
    val isbn: String,
    @field:NotBlank(message = "The book title must be defined.") val title: String,
    @field:NotBlank(message = "The book author must be defined.") val author: String,
    @field:NotNull(message = "The book price must be defined.")
    @field:Positive(message = "The book price must be greater than zero.")
    val price: Double,
    @Version val version: Int
) {
  constructor(
      isbn: String,
      title: String,
      author: String,
      price: Double,
  ) : this(null, isbn, title, author, price, 0) {}
}
