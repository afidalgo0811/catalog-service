package com.afidalgo.catalogservice

import com.afidalgo.catalogservice.domain.Book
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ActiveProfiles

@JsonTest
@ActiveProfiles("integration")
class BookJsonTests {
  @Autowired private lateinit var json: JacksonTester<Book>

  @Throws(Exception::class)
  @Test
  fun testSerialize() {
    val book = Book("1234567890", "Title", "Author", 9.90)
    val jsonContent = json.write(book)
    assertThat(jsonContent).extractingJsonPathStringValue("@.isbn").isEqualTo(book.isbn)
    assertThat(jsonContent).extractingJsonPathStringValue("@.title").isEqualTo(book.title)
    assertThat(jsonContent).extractingJsonPathStringValue("@.author").isEqualTo(book.author)
    assertThat(jsonContent).extractingJsonPathNumberValue("@.price").isEqualTo(book.price)
  }

  @Throws(Exception::class)
  @Test
  fun testDeserialize() {
    val content =
        """ 
            {
            "isbn": "1234567890",
            "title": "Title",
            "author": "Author",
            "price": 9.90
            }
        """
    assertThat(json.parse(content))
        .usingRecursiveComparison()
        .isEqualTo(Book("1234567890", "Title", "Author", 9.90))
  }
}
