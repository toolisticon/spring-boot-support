package io.toolisticon.spring.condition

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.context.annotation.UserConfigurations
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class ConditionalOnPropertyValuesTest {

  private val contextRunner = ApplicationContextRunner()

  @Test
  fun `matches a property with an allowed value`() {
    contextRunner
      .withPropertyValues("feature.mode=enabled")
      .withConfiguration(UserConfigurations.of(AllowedValueConfiguration::class.java))
      .run { assertThat(it).hasBean("allowedValue") }
  }

  @Test
  fun `matches each provided allowed value`() {
    contextRunner
      .withPropertyValues("feature.mode=maintenance")
      .withConfiguration(UserConfigurations.of(AllowedValueConfiguration::class.java))
      .run { assertThat(it).hasBean("allowedValue") }
  }

  @Test
  fun `does not match a property with a value outside the allowed values`() {
    contextRunner
      .withPropertyValues("feature.mode=disabled")
      .withConfiguration(UserConfigurations.of(AllowedValueConfiguration::class.java))
      .run { assertThat(it).doesNotHaveBean("allowedValue") }
  }

  @Test
  fun `does not match a missing property by default`() {
    contextRunner
      .withConfiguration(UserConfigurations.of(AllowedValueConfiguration::class.java))
      .run { assertThat(it).doesNotHaveBean("allowedValue") }
  }

  @Test
  fun `matches a missing property when configured to do so`() {
    contextRunner
      .withConfiguration(UserConfigurations.of(MatchMissingPropertyConfiguration::class.java))
      .run { assertThat(it).hasBean("matchMissingProperty") }
  }

  @Test
  fun `matches only when all named properties have allowed values`() {
    contextRunner
      .withPropertyValues("feature.mode=enabled", "feature.transport=async")
      .withConfiguration(UserConfigurations.of(MultiplePropertiesConfiguration::class.java))
      .run { assertThat(it).hasBean("multipleProperties") }

    contextRunner
      .withPropertyValues("feature.mode=enabled", "feature.transport=blocking")
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
  @ConditionalOnPropertyValues(
    prefix = "feature",
    name = ["mode"],
    havingValue = ["enabled", "maintenance"]
  )
  class AllowedValueConfiguration {

    @Bean
    fun allowedValue() = "allowed"
  }

  @Configuration(proxyBeanMethods = false)
  @ConditionalOnPropertyValues(
    prefix = "feature",
    name = ["mode"],
    havingValue = ["enabled"],
    matchIfMissing = true
  )
  class MatchMissingPropertyConfiguration {

    @Bean
    fun matchMissingProperty() = "missing"
  }

  @Configuration(proxyBeanMethods = false)
  @ConditionalOnPropertyValues(
    prefix = "feature",
    name = ["mode", "transport"],
    havingValue = ["enabled", "async"]
  )
  class MultiplePropertiesConfiguration {

    @Bean
    fun multipleProperties() = "multiple"
  }

  @Configuration(proxyBeanMethods = false)
  @ConditionalOnPropertyValues(
    prefix = "feature",
    value = ["mode"],
    havingValue = ["enabled"]
  )
  class ValueAliasConfiguration {

    @Bean
    fun valueAlias() = "alias"
  }

  @Configuration(proxyBeanMethods = false)
  @ConditionalOnPropertyValues(name = ["mode"], havingValue = ["enabled"])
  class NoPrefixConfiguration {

    @Bean
    fun noPrefix() = "no-prefix"
  }

  @Configuration(proxyBeanMethods = false)
  @ConditionalOnPropertyValues(havingValue = ["enabled"])
  class NoPropertyNamesConfiguration {

    @Bean
    fun noPropertyNames() = "no-names"
  }
}
