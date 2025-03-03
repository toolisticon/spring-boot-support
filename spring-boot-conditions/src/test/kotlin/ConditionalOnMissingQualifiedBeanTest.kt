package io.toolisticon.spring.condition

import com.sun.tools.javac.util.AbstractDiagnosticFormatter
import io.toolisticon.spring.condition.fixture.MyComponent
import io.toolisticon.spring.condition.fixture.MyTestConfiguration
import io.toolisticon.spring.condition.fixture.SimpleConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.context.annotation.UserConfigurations
import org.springframework.boot.test.context.runner.ApplicationContextRunner

class ConditionalOnMissingQualifiedBeanTest {

  private val contextRunner = ApplicationContextRunner()

  @Test
  fun `should load qualified default bean`() {
    contextRunner
      .withConfiguration(UserConfigurations.of(MyTestConfiguration::class.java))
      .run {
        assertThat(it.getBean(MyComponent::class.java)).isNotNull
        val component: MyComponent = it.getBean(MyComponent::class.java)
        assertThat(component.qualifier).isEqualTo("qualifier1")
      }
  }

  @Test
  fun `should load user qualified bean`() {
    contextRunner
      .withConfiguration(UserConfigurations.of(SimpleConfiguration::class.java))
      .withConfiguration(UserConfigurations.of(MyTestConfiguration::class.java))
      .run {
        assertThat(it.getBean(MyComponent::class.java)).isNotNull
        val component: MyComponent = it.getBean(MyComponent::class.java)
        assertThat(component.qualifier).isEqualTo("qualifier2")
      }
    }

}
