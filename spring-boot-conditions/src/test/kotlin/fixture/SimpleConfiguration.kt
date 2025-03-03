package io.toolisticon.spring.condition.fixture

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SimpleConfiguration {

  @Bean
  @Qualifier(value = "qualifier")
  fun component2(): MyComponent = MyComponent("qualifier2")

}
