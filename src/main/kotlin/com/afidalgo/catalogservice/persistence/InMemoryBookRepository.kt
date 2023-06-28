package com.afidalgo.catalogservice.persistence

import com.afidalgo.catalogservice.domain.Book
import com.afidalgo.catalogservice.domain.BookRepository
import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Repository

@Repository
class InMemoryBookRepository : BookRepository {

  val books: MutableMap<String, Book> = ConcurrentHashMap()
  override fun findAll(): Iterable<Book> {
    return books.values
  }

  override fun findByIsbn(isbn: String): Book? {
    return if (existsByIsbn(isbn)) books[isbn] else null
  }

  override fun existsByIsbn(isbn: String): Boolean {
    return books[isbn] != null
  }

  override fun save(book: Book): Book {
    books[book.isbn] = book
    return book
  }

  override fun deleteByIsbn(isbn: String) {
    books.remove(isbn)
  }
}
