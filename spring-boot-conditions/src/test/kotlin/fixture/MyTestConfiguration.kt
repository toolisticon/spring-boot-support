package io.toolisticon.spring.condition.fixture

import io.toolisticon.spring.condition.ConditionalOnMissingQualifiedBean
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MyTestConfiguration {

  @ConditionalOnMissingQualifiedBean(beanClass = MyComponent::class, qualifier = "qualifier")
  @Bean
  @Qualifier("qualifier")
  fun component1(): MyComponent = MyComponent("qualifier1")
}
