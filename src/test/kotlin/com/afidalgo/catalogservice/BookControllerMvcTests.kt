package com.afidalgo.catalogservice

import com.afidalgo.catalogservice.config.SecurityConfig
import com.afidalgo.catalogservice.domain.BookNotFoundException
import com.afidalgo.catalogservice.domain.BookService
import com.afidalgo.catalogservice.web.BookController
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BookController::class)
@Import(SecurityConfig::class)
class BookControllerMvcTests {
  @Autowired lateinit var mockMvc: MockMvc
  @MockBean lateinit var bookService: BookService
  @MockBean lateinit var jwtDecoder: JwtDecoder

  @Throws(Exception::class)
  @Test
  fun whenGetBookNotExistingThenShouldReturn404() {
    val isbn = "73737313940"
    given(bookService.viewBookDetails(isbn)).willThrow(BookNotFoundException(isbn))
    mockMvc.perform(get("/books/$isbn")).andExpect(status().isNotFound)
  }

  @Test
  @Throws(Exception::class)
  fun whenDeleteBookWithEmployeeRoleThenShouldReturn204() {
    val isbn = "7373731394"
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/books/$isbn")
                .with(
                    SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(SimpleGrantedAuthority("ROLE_employee"))))
        .andExpect(MockMvcResultMatchers.status().isNoContent)
  }

  @Test
  @Throws(Exception::class)
  fun whenDeleteBookWithCustomerRoleThenShouldReturn403() {
    val isbn = "7373731394"
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/books/$isbn")
                .with(
                    SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(SimpleGrantedAuthority("ROLE_customer"))))
        .andExpect(MockMvcResultMatchers.status().isForbidden)
  }

  @Test
  @Throws(Exception::class)
  fun whenDeleteBookWithCustomerRoleThenShouldReturn401() {
    val isbn = "7373731394"
    mockMvc
        .perform(MockMvcRequestBuilders.delete("/books/$isbn"))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized)
  }
}
