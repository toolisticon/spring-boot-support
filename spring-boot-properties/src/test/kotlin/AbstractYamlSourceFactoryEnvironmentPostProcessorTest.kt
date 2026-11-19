package io.toolisticon.spring.properties

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalStateException
import org.junit.jupiter.api.Test
import org.springframework.boot.SpringApplication
import org.springframework.core.Ordered
import org.springframework.mock.env.MockEnvironment

class AbstractYamlSourceFactoryEnvironmentPostProcessorTest {

  @Test
  fun `loads YAML defaults into the environment`() {
    val environment = MockEnvironment()

    TestEnvironmentPostProcessor(defaultSource())
      .postProcessEnvironment(environment, SpringApplication())

    assertThat(environment.getProperty("test.foo")).isEqualTo("default-foo")
    assertThat(environment.getProperty("test.zee")).isEqualTo("12")
    assertThat(environment.getProperty("test.baz")).isEqualTo("true")
  }

  @Test
  fun `allows application properties to override YAML defaults`() {
    val environment = MockEnvironment()
      .withProperty("test.foo", "application-foo")

    TestEnvironmentPostProcessor(defaultSource())
      .postProcessEnvironment(environment, SpringApplication())

    assertThat(environment.getProperty("test.foo")).isEqualTo("application-foo")
    assertThat(environment.getProperty("test.zee")).isEqualTo("12")
  }

  @Test
  fun `does not load defaults when the source is disabled`() {
    val source = defaultSource()
    val environment = MockEnvironment()
      .withProperty("io.toolisticon.spring.properties.${source.name}.enabled", "false")

    TestEnvironmentPostProcessor(source)
      .postProcessEnvironment(environment, SpringApplication())

    assertThat(environment.getProperty("test.foo")).isNull()
    assertThat(environment.propertySources.contains(source.name)).isFalse()
  }

  @Test
  fun `fails with an explanatory error when the YAML resource cannot be loaded`() {
    val source = YamlPropertySource("missingDefaults", "missing-defaults.yaml")
    val environment = MockEnvironment()

    assertThatIllegalStateException()
      .isThrownBy {
        TestEnvironmentPostProcessor(source)
          .postProcessEnvironment(environment, SpringApplication())
      }
      .withMessage("Could not load 'missingDefaults' from 'missing-defaults.yaml'")
  }

  @Test
  fun `uses the order configured on the YAML source`() {
    val source = YamlPropertySource("orderedDefaults", "environment-post-processor-defaults.yaml", 42)

    assertThat(TestEnvironmentPostProcessor(source).order).isEqualTo(42)
  }

  @Test
  fun `uses the low precedence default order`() {
    assertThat(defaultSource().order).isEqualTo(Ordered.LOWEST_PRECEDENCE)
  }

  @Test
  fun `exposes YAML property sources as Java records with a convenience constructor`() {
    val twoArgumentConstructor = YamlPropertySource::class.java.getConstructor(String::class.java, String::class.java)
    val source = twoArgumentConstructor.newInstance("javaDefaults", "environment-post-processor-defaults.yaml")

    assertThat(YamlPropertySource::class.java.isRecord).isTrue()
    assertThat(source.name).isEqualTo("javaDefaults")
    assertThat(source.resourceLocation).isEqualTo("environment-post-processor-defaults.yaml")
    assertThat(source.order).isEqualTo(Ordered.LOWEST_PRECEDENCE)
  }

  private fun defaultSource() = YamlPropertySource("testDefaults", "environment-post-processor-defaults.yaml")

  private class TestEnvironmentPostProcessor(
    private val source: YamlPropertySource
  ) : AbstractYamlSourceFactoryEnvironmentPostProcessor() {

    override fun getYamlPropertySource(): YamlPropertySource = source
  }
}
