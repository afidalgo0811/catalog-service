package com.afidalgo.catalogservice

import com.afidalgo.catalogservice.domain.Book
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import dasniko.testcontainers.keycloak.KeycloakContainer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@Testcontainers
class CatalogServiceApplicationTests(@Autowired val webTestClient: WebTestClient) {

  companion object {

    private var bjornTokens: KeycloakToken? = null
    private var isabelleTokens: KeycloakToken? = null
    @JvmStatic
    @Container
    private val keycloakContainer =
        KeycloakContainer("quay.io/keycloak/keycloak:19.0")
            .withRealmImportFile("test-realm-config.json")

    @JvmStatic
    @DynamicPropertySource
    fun dynamicProperties(registry: DynamicPropertyRegistry) {
      registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri") {
        keycloakContainer.authServerUrl + "realms/PolarBookshop"
      }
    }

    private fun authenticateWith(
        username: String,
        password: String,
        webClient: WebClient
    ): KeycloakToken? =
        webClient
            .post()
            .body(
                BodyInserters.fromFormData("grant_type", "password")
                    .with("client_id", "polar-test")
                    .with("username", username)
                    .with("password", password))
            .retrieve()
            .bodyToMono(KeycloakToken::class.java)
            .block()

    @JvmStatic
    @BeforeAll
    fun setUp() {
      val webClient =
          WebClient.builder()
              .baseUrl(
                  keycloakContainer.authServerUrl +
                      "realms/PolarBookshop/protocol/openid-connect/token")
              .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
              .build()
      isabelleTokens = authenticateWith("isabelle", "password", webClient)
      bjornTokens = authenticateWith("bjorn", "password", webClient)
    }
  }

  @Test
  fun whenPostRequestThenBookCreated() {
    val expectedBook = Book("1231231231", "Title", "Author", 9.90, "publisher")
    webTestClient
        .post()
        .uri("/books")
        .headers { isabelleTokens?.let { it1 -> it.setBearerAuth(it1.accessToken) } }
        .bodyValue(expectedBook)
        .exchange()
        .expectStatus()
        .isCreated
        .expectBody(Book::class.java)
        .value { actualBook ->
          assertThat(actualBook).isNotNull
          assertThat(actualBook.isbn).isEqualTo(expectedBook.isbn)
        }
  }

  @Test
  fun whenPostRequestUnauthorizedThen403() {
    val expectedBook = Book("1231231231", "Title", "Author", 9.90, "PolarSophia")
    webTestClient
        .post()
        .uri("/books")
        .headers { bjornTokens?.let { it1 -> it.setBearerAuth(it1.accessToken) } }
        .bodyValue(expectedBook)
        .exchange()
        .expectStatus()
        .isForbidden
  }

  @Test
  fun whenPostRequestUnauthenticatedThen401() {
    val expectedBook = Book("1231231231", "Title", "Author", 9.90, "PolarSophia")
    webTestClient
        .post()
        .uri("/books")
        .bodyValue(expectedBook)
        .exchange()
        .expectStatus()
        .isUnauthorized
  }
}

data class KeycloakToken(@JsonProperty("access_token") val accessToken: String) {
  companion object {
    @JsonCreator fun create(accessToken: String) = KeycloakToken(accessToken)
  }
}
