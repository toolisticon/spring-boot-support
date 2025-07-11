package io.toolisticon.spring.properties

import io.toolisticon.spring.properties.fixture.MyProperties
import io.toolisticon.spring.properties.fixture.MyTestConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.context.annotation.UserConfigurations
import org.springframework.boot.test.context.runner.ApplicationContextRunner

class YamlPropertySourceFactoryTest {

  private val contextRunner = ApplicationContextRunner()
    .withConfiguration(UserConfigurations.of(MyTestConfiguration::class.java))

  @Test
  fun `properties are loaded from defaults`() {
    contextRunner
      .run {

        assertThat(it.getBean(MyProperties::class.java)).isNotNull
        val props: MyProperties = it.getBean(MyProperties::class.java)

        assertThat(props.foo).isEqualTo("bar")
        assertThat(props.zee).isEqualTo(12)
        assertThat(props.baz).isTrue()
      }
  }

  @Test
  fun `properties are loaded from defaults and overridden`() {
    contextRunner
      .withPropertyValues(
        "test.foo=nobar",
        "test.zee=42"
      )
      .run {
        assertThat(it.getBean(MyProperties::class.java)).isNotNull
        val props: MyProperties = it.getBean(MyProperties::class.java)

        assertThat(props.foo).isEqualTo("nobar")
        assertThat(props.zee).isEqualTo(42)
        assertThat(props.baz).isTrue()
      }
  }

}


