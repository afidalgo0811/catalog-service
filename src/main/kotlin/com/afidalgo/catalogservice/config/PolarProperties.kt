package com.afidalgo.catalogservice.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "polar")
class PolarProperties {

  /** Welcome message to polar users */
  lateinit var greetings: String
}
