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
    fun removeBookFromCatalog(isbn: String) {
        bookRepository.deleteByIsbn(isbn)
    }
    fun editBookDetails(isbn: String, book: Book): Book? {
        return bookRepository.findByIsbn(isbn)
            ?.let {
                val upDatedBook = Book(it.isbn, book.title, book.author, book.price)
                return bookRepository.save(upDatedBook)
            }
    }
}
