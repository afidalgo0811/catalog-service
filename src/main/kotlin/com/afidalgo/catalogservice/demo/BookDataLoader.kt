package com.afidalgo.catalogservice.demo

import com.afidalgo.catalogservice.domain.Book
import com.afidalgo.catalogservice.domain.BookRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
@Profile("test-data")
class BookDataLoader(val bookRepository: BookRepository) {
  @EventListener(ApplicationReadyEvent::class)
  fun loadBookTestData() {
    bookRepository.deleteAll()
    val book1 = Book("1234567891", "Northern Lights", "Lyra Silverstar", 9.90, "publisher")
    val book2 = Book("1234567892", "Polar Journey", "Iorek Polarson", 12.90, "publisher")
    bookRepository.saveAll(listOf(book1, book2))
  }
}
