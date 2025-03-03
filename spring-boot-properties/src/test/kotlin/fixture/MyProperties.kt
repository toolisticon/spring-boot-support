package io.toolisticon.spring.properties.fixture

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "test")
data class MyProperties(
  val foo: String,
  val zee: Int,
  val baz: Boolean
)
