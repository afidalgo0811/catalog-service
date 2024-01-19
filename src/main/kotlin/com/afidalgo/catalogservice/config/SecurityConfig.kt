package com.afidalgo.catalogservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

  @Bean
  @Throws(Exception::class)
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    return http
        .authorizeHttpRequests {
          it.requestMatchers(HttpMethod.GET, "/", "/books/**")
              .permitAll()
              .anyRequest()
              .hasRole("employee")
        }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .oauth2ResourceServer { oauth2 -> oauth2.jwt(Customizer.withDefaults()) }
        .csrf { it: CsrfConfigurer<HttpSecurity> -> it.disable() }
        .build()
    // using kotlin dsl
    //    http.invoke {
    //      authorizeRequests {
    //        authorize(AntPathRequestMatcher("/", HttpMethod.GET.name()), permitAll)
    //        authorize(AntPathRequestMatcher("/books/**", HttpMethod.GET.name()), permitAll)
    //        authorize(anyRequest, hasRole("employee"))
    //      }
    //      oauth2ResourceServer { jwt {} }
    //      sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
    //      csrf { disabled }
    //    }
    //    return http.build()
  }

  @Bean
  fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
    val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_")
    jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles")
    val jwtAuthenticationConverter = JwtAuthenticationConverter()
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter)
    return jwtAuthenticationConverter
  }
}
