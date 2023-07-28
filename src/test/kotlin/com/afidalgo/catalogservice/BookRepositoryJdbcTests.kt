package com.afidalgo.catalogservice

import com.afidalgo.catalogservice.config.DataConfig
import com.afidalgo.catalogservice.domain.Book
import com.afidalgo.catalogservice.domain.BookRepository
import io.kotest.matchers.longs.shouldBeInRange
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@DataJdbcTest
@Import(DataConfig::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
@Testcontainers
class BookRepositoryJdbcTests {
  @Autowired lateinit var bookRepository: BookRepository
  @Autowired lateinit var jdbcAggregateTemplate: JdbcAggregateTemplate
  @Autowired private lateinit var jdbcTemplate: JdbcTemplate

  companion object {
    @Container private val postgreSQLContainer = PostgreSQLContainer<Nothing>("postgres:latest")

    @DynamicPropertySource
    @JvmStatic
    fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
      registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
      registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
      registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
    }
  }

  @Test
  fun findBookByIsbnWhenExisting() {
    val bookIsbn = "1234561237"
    val book = Book(bookIsbn, "Title", "Author", 12.90)
    jdbcAggregateTemplate.insert(book)
    val actualBook = bookRepository.findByIsbn(bookIsbn)
    actualBook.shouldNotBeNull()
  }

  @Test
  fun `when database is connected then it should be Postgres version 15`() {
    val actualDatabaseVersion = jdbcTemplate.queryForObject("SELECT version()", String::class.java)
    actualDatabaseVersion shouldContain "PostgreSQL 15.3"
  }

  @Test
  fun `when record is saved then the id is populated`() {
    val bookIsbn = "1234561237"
    val book = Book(bookIsbn, "Title", "Author", 12.90)
    val actual = jdbcAggregateTemplate.insert(book)
    actual.id?.shouldBeInRange((1..Long.MAX_VALUE))
  }
}
