package com.afidalgo.catalogservice.web

import com.afidalgo.catalogservice.domain.Book
import com.afidalgo.catalogservice.domain.BookService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("books")
class BookController(val bookService: BookService) {

  companion object {
    private val logger = LoggerFactory.getLogger(this::class.java)
  }

  @GetMapping
  fun get(): Iterable<Book> {
    logger.info("Fetching the list of books in the catalog")
    return bookService.viewBookList()
  }

  @GetMapping("{isbn}")
  fun getByIsbn(@PathVariable isbn: String): Book {
    logger.info("Fetching the book with ISBN {} from the catalog", isbn)
    return bookService.viewBookDetails(isbn)
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun post(
      @Valid @RequestBody book: Book,
  ): Book? {
    logger.info("Adding a new book to the catalog with ISBN {}", book.isbn)
    return bookService.addBookToCatalog(book)
  }

  @DeleteMapping("{isbn}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun delete(@PathVariable isbn: String) {
    logger.info("Deleting book with ISBN {}", isbn)
    bookService.removeBookFromCatalog(isbn)
  }

  @PutMapping("{isbn}")
  fun put(
      @PathVariable isbn: String,
      @Valid @RequestBody book: Book,
  ): Book? {
    logger.info("Updating book with ISBN {}", isbn)
    return bookService.editBookDetails(isbn, book)
  }
}
