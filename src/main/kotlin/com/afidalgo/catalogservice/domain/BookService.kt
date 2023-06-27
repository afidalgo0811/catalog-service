package com.afidalgo.catalogservice.domain

import org.springframework.stereotype.Service

@Service
class BookService(val bookRepository: BookRepository) {

    fun viewBookList(): Iterable<Book> {
        return bookRepository.findAll()
    }
    fun viewBookDetails(isbn: String): Book {
        return bookRepository.findByIsbn(isbn) ?: throw BookNotFoundException(isbn)
    }
    fun addBookToCatalog(book: Book): Book? {
        if (bookRepository.existsByIsbn(book.isbn)) {
            throw BookAlreadyExistsException(book.isbn)
        }
        return bookRepository.save(book)
    }
    fun removeBook(book: Book) {
        bookRepository.deleteByIsbn(book.isbn)
    }
    fun editBookDetails(isbn: String, book: Book): Book? {
        return bookRepository.findByIsbn(isbn)
            ?.let {
                val test = Book(it.isbn, it.title, it.author, it.price)
                return bookRepository.save(test)
            }
    }
}