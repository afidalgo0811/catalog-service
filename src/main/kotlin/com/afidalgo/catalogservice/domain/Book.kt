package com.afidalgo.catalogservice.domain

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import java.time.Instant
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("book")
data class Book(
    @Column("id") @Id val id: Long?,
    @Column("publisher") val publisher: String?,
    @field:NotBlank(message = "The book ISBN must be defined.")
    @field:Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "The ISBN format must be valid.")
    @Column("isbn")
    val isbn: String,
    @field:NotBlank(message = "The book title must be defined.") @Column("title") val title: String,
    @field:NotBlank(message = "The book author must be defined.")
    @Column("author")
    val author: String,
    @field:NotNull(message = "The book price must be defined.")
    @field:Positive(message = "The book price must be greater than zero.")
    @Column("price")
    val price: Double,
    @CreatedDate val createdDate: Instant?,
    @LastModifiedDate val lastModifiedDate: Instant?,
    @CreatedBy val createdBy: String?,
    @LastModifiedBy val lastModifiedBy: String?,
    @Column("version") @Version val version: Int
) {
  constructor(
      isbn: String,
      title: String,
      author: String,
      price: Double,
      publisher: String,
  ) : this(null, publisher, isbn, title, author, price, null, null, null, null, 0)

  companion object {
    fun create(isbn: String, title: String, author: String, price: Double, publisher: String) =
        Book(null, publisher, isbn, title, author, price, null, null, null, null, 0)
  }
}
