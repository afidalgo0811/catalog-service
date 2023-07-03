package com.afidalgo.catalogservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan @SpringBootApplication class CatalogServiceApplication

fun main(args: Array<String>) {
  runApplication<CatalogServiceApplication>(*args)
}
