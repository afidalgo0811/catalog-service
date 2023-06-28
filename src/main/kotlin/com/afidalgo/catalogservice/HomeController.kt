package com.afidalgo.catalogservice

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {

  @GetMapping("/")
  fun greetings(): String {
    return "greeting from the book store."
  }
}
