package com.afidalgo.catalogservice

import com.afidalgo.catalogservice.domain.Book
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.test.context.ActiveProfiles

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("integration")
class BookValidationTest {

  private lateinit var validator: Validator
  private var messageList: List<String> =
      listOf(
          "The ISBN format must be valid.",
          "The book title must be defined.",
          "The book author must be defined.",
          "The book price must be greater than zero.")

  @BeforeAll
  fun setUp() {
    val validatorFactory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
    validator = validatorFactory.validator
  }

  @Test
  fun whenAllFieldsCorrectThenValidationSucceeds() {
    val book = Book("1234567890", "Title", "Author", 9.90)
    val violations: Set<ConstraintViolation<Book>> = validator.validate(book)
    assertThat(violations).isEmpty()
  }

  @Test
  fun whenIsbnIsNotCorrectThenValidationFails() {
    val book = Book("1234", "Title", "Author", 9.90)
    val violations: Set<ConstraintViolation<Book>> = validator.validate(book)
    assertThat(violations).hasSize(1)
    assertThat(violations.iterator().next().message).isEqualTo(messageList[0])
  }

  @Test
  fun whenTitleIsNotCorrectThenValidationFails() {
    val book = Book("1234567890", "", "Author", 9.90)
    val violations: Set<ConstraintViolation<Book>> = validator.validate(book)
    assertThat(violations).hasSize(1)
    assertThat(violations.iterator().next().message).isEqualTo(messageList[1])
  }

  @Test
  fun whenAuthorIsNotCorrectThenValidationFails() {
    val book = Book("1234567890", "Title", "", 9.90)
    val violations: Set<ConstraintViolation<Book>> = validator.validate(book)
    assertThat(violations).hasSize(1)
    assertThat(violations.iterator().next().message).isEqualTo(messageList[2])
  }

  @Test
  fun whenPriceIsNotCorrectThenValidationFails() {
    val book = Book("1234567890", "Title", "Author", 0.0)
    val violations: Set<ConstraintViolation<Book>> = validator.validate(book)
    assertThat(violations).hasSize(1)
    assertThat(violations.iterator().next().message).isEqualTo(messageList[3])
  }
}
