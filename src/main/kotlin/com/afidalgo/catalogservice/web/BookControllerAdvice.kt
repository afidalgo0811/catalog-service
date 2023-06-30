package com.afidalgo.catalogservice.web

import com.afidalgo.catalogservice.domain.BookAlreadyExistsException
import com.afidalgo.catalogservice.domain.BookNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class BookControllerAdvice {
  @ExceptionHandler(BookNotFoundException::class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  fun bookNotFoundHandler(ex: BookNotFoundException): String {
    return ex.message!!
  }

  @ExceptionHandler(BookAlreadyExistsException::class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  fun bookAlreadyExistHandler(ex: BookAlreadyExistsException): String {
    return ex.message!!
  }

  @ExceptionHandler(MethodArgumentNotValidException::class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  fun handleValidationException(ex: MethodArgumentNotValidException): MutableMap<String, String> {
    val errors: MutableMap<String, String> = mutableMapOf()
    ex.bindingResult.allErrors.forEach {
      it as FieldError
      val fieldName = it.field
      val errorMessage = it.defaultMessage!!
      errors[fieldName] = errorMessage
    }
    return errors
  }
}
