package com.afidalgo.catalogservice.config

import java.util.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing
import org.springframework.security.core.context.SecurityContextHolder

@Configuration
@EnableJdbcAuditing
class DataConfig {

  @Bean
  fun auditorAware(): AuditorAware<String> {
    return AuditorAware<String> {
      Optional.ofNullable(SecurityContextHolder.getContext())
          .map { it.authentication }
          .filter { it.isAuthenticated }
          .map { it.name }
    }
  }
  //    @Bean
  //    fun auditorAware(): AuditorAware<String> {
  //        return AuditorAware<String> {
  //            Optional.ofNullable(SecurityContextHolder.getContext())
  //                .map(SecurityContext::getAuthentication)
  //                .filter(Authentication::isAuthenticated)
  //                .map(Authentication::getName)
  //        }
  //    }
}
