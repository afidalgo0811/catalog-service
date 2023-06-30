package com.afidalgo.catalogservice

import com.afidalgo.catalogservice.domain.BookNotFoundException
import com.afidalgo.catalogservice.domain.BookService
import com.afidalgo.catalogservice.web.BookController
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BookController::class)
class BookControllerMvcTests {
  @Autowired lateinit var mockMvc: MockMvc
  @MockBean lateinit var bookService: BookService

  @Throws(Exception::class)
  @Test
  fun whenGetBookNotExistingThenShouldReturn404() {
    val isbn = "73737313940"
    given(bookService.viewBookDetails(isbn)).willAnswer { BookNotFoundException::class }
    mockMvc.perform(get("/books$isbn")).andExpect(status().isNotFound)
  }
}
