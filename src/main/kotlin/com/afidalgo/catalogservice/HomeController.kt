package com.afidalgo.catalogservice

import com.afidalgo.catalogservice.config.PolarProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController(val polarProperties: PolarProperties) {

  @GetMapping("/")
  fun greetings(): String {
    return polarProperties.greetings
  }
}
