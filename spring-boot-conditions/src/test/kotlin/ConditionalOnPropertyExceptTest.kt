package io.toolisticon.spring.condition

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.context.annotation.UserConfigurations
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class ConditionalOnPropertyExceptTest {

  private val contextRunner = ApplicationContextRunner()

  @Test
  fun `matches a present property with a non-excluded value`() {
    contextRunner
      .withPropertyValues("feature.mode=enabled")
      .withConfiguration(UserConfigurations.of(NonExcludedValueConfiguration::class.java))
      .run { assertThat(it).hasBean("nonExcludedValue") }
  }

  @Test
  fun `does not match a property with an excluded value`() {
    contextRunner
      .withPropertyValues("feature.mode=disabled")
      .withConfiguration(UserConfigurations.of(NonExcludedValueConfiguration::class.java))
      .run { assertThat(it).doesNotHaveBean("nonExcludedValue") }
  }

  @Test
  fun `does not match a missing property by default`() {
    contextRunner
      .withConfiguration(UserConfigurations.of(NonExcludedValueConfiguration::class.java))
      .run { assertThat(it).doesNotHaveBean("nonExcludedValue") }
  }

  @Test
  fun `matches a missing property when configured to do so`() {
    contextRunner
      .withConfiguration(UserConfigurations.of(MatchMissingPropertyConfiguration::class.java))
      .run { assertThat(it).hasBean("matchMissingProperty") }
  }

  @Test
  fun `matches only when all named properties have non-excluded values`() {
    contextRunner
      .withPropertyValues("feature.mode=enabled", "feature.transport=async")
      .withConfiguration(UserConfigurations.of(MultiplePropertiesConfiguration::class.java))
      .run { assertThat(it).hasBean("multipleProperties") }

    contextRunner
      .withPropertyValues("feature.mode=enabled", "feature.transport=disabled")
      .withConfiguration(UserConfigurations.of(MultiplePropertiesConfiguration::class.java))
      .run { assertThat(it).doesNotHaveBean("multipleProperties") }
  }

  @Test
  fun `supports value as an alias for name`() {
    contextRunner
      .withPropertyValues("feature.mode=enabled")
      .withConfiguration(UserConfigurations.of(ValueAliasConfiguration::class.java))
      .run { assertThat(it).hasBean("valueAlias") }
  }

  @Test
  fun `supports property names without a prefix`() {
    contextRunner
      .withPropertyValues("mode=enabled")
      .withConfiguration(UserConfigurations.of(NoPrefixConfiguration::class.java))
      .run { assertThat(it).hasBean("noPrefix") }
  }

  @Test
  fun `does not match when no property names are configured`() {
    contextRunner
      .withConfiguration(UserConfigurations.of(NoPropertyNamesConfiguration::class.java))
      .run { assertThat(it).doesNotHaveBean("noPropertyNames") }
  }

  @Configuration(proxyBeanMethods = false)
  @ConditionalOnPropertyExcept(
    prefix = "feature",
    name = ["mode"],
    havingValue = ["disabled", "maintenance"]
  )
  class NonExcludedValueConfiguration {

    @Bean
    fun nonExcludedValue() = "non-excluded"
  }

  @Configuration(proxyBeanMethods = false)
  @ConditionalOnPropertyExcept(
    prefix = "feature",
    name = ["mode"],
    havingValue = ["disabled"],
    matchIfMissing = true
  )
  class MatchMissingPropertyConfiguration {

    @Bean
    fun matchMissingProperty() = "missing"
  }

  @Configuration(proxyBeanMethods = false)
  @ConditionalOnPropertyExcept(
    prefix = "feature",
    name = ["mode", "transport"],
    havingValue = ["disabled"]
  )
  class MultiplePropertiesConfiguration {

    @Bean
    fun multipleProperties() = "multiple"
  }

  @Configuration(proxyBeanMethods = false)
  @ConditionalOnPropertyExcept(
    prefix = "feature",
    value = ["mode"],
    havingValue = ["disabled"]
  )
  class ValueAliasConfiguration {

    @Bean
    fun valueAlias() = "alias"
  }

  @Configuration(proxyBeanMethods = false)
  @ConditionalOnPropertyExcept(name = ["mode"], havingValue = ["disabled"])
  class NoPrefixConfiguration {

    @Bean
    fun noPrefix() = "no-prefix"
  }

  @Configuration(proxyBeanMethods = false)
  @ConditionalOnPropertyExcept(havingValue = ["disabled"])
  class NoPropertyNamesConfiguration {

    @Bean
    fun noPropertyNames() = "no-names"
  }
}
