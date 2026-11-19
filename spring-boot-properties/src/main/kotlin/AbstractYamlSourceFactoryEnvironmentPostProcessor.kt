package io.toolisticon.spring.properties

import org.springframework.boot.SpringApplication
import org.springframework.boot.EnvironmentPostProcessor
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.core.Ordered
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.getProperty
import org.springframework.core.io.ClassPathResource
import java.io.IOException
import kotlin.jvm.JvmRecord

/**
 * Base class for library-provided YAML defaults that must be available before auto-configuration is evaluated.
 *
 * Spring Boot invokes environment post-processors during environment preparation, before it evaluates
 * auto-configuration conditions or binds configuration properties. A subclass supplies one YAML resource through
 * [getYamlPropertySource], including its execution order. Sources created with the two-argument
 * [YamlPropertySource] constructor use [Ordered.LOWEST_PRECEDENCE], ensuring they run after normal configuration
 * data has been loaded. It must also be registered under the
 * `org.springframework.boot.env.EnvironmentPostProcessor` key in `META-INF/spring.factories`; declaring the class
 * alone does not make Spring Boot discover it.
 *
 * This is a Java-compatible extension point. Java subclasses override `getYamlPropertySource()` and create
 * [YamlPropertySource] with its public two-argument constructor.
 *
 * The processor loads the resource with [YamlPropertySourceLoader] and appends the resulting property sources to the
 * environment. Appending them gives existing application property sources higher precedence, so application
 * configuration can override library defaults. The source is enabled by default and can be disabled with
 * `io.toolisticon.spring.properties.<source-name>.enabled=false`, where `<source-name>` is
 * [YamlPropertySource.name].
 *
 * An unreadable YAML resource fails startup with an [IllegalStateException]. Use
 * [YamlPropertySourceFactory] instead when defaults are local to a configuration class and do not need to influence
 * auto-configuration.
 */
abstract class AbstractYamlSourceFactoryEnvironmentPostProcessor : EnvironmentPostProcessor, Ordered {

  /**
   * Returns the execution order defined by this processor's YAML source.
   *
   * Lower values run first. Because each processor appends its source to the environment, a source that runs first
   * has higher lookup precedence than later YAML sources. The returned source definition must therefore be stable.
   */
  final override fun getOrder(): Int = getYamlPropertySource().order

  /**
   * Adds this processor's configured YAML defaults to [environment] when the source is enabled.
   *
   * The property source name is used both to name the loaded Spring property source and to derive its enablement key.
   * Every source produced by [YamlPropertySourceLoader] is added with `addLast`, preserving application properties as
   * higher-precedence overrides. This template method is final; subclasses customize it only through
   * [getYamlPropertySource].
   *
   * @param environment the mutable application environment being prepared
   * @param application the Spring application that is being prepared
   * @throws IllegalStateException if the configured YAML resource cannot be loaded
   */
  final override fun postProcessEnvironment(environment: ConfigurableEnvironment, application: SpringApplication) {

    val yamlPropertySource = getYamlPropertySource()
    val propertySourceName = yamlPropertySource.name
    if (!environment.getProperty<Boolean>("io.toolisticon.spring.properties.$propertySourceName.enabled", true)) {
      return
    }
    try {
      YamlPropertySourceLoader()
        .load(propertySourceName, ClassPathResource(yamlPropertySource.resourceLocation))
        .forEach { propertySource -> environment.propertySources.addLast(propertySource) }
    } catch (exception: IOException) {
      throw IllegalStateException("Could not load '$propertySourceName' from '${yamlPropertySource.resourceLocation}'", exception)
    } catch (exception: IllegalStateException) {
      throw IllegalStateException("Could not load '$propertySourceName' from '${yamlPropertySource.resourceLocation}'", exception)
    }
  }

  /**
   * Defines the unique name and classpath location of the YAML defaults to load.
   *
   * The returned [YamlPropertySource.name] must be stable and unique within the application: it identifies the
   * property source and forms the enablement key
   * `io.toolisticon.spring.properties.<name>.enabled`. The resource location is resolved as a classpath resource.
   *
   * @return the YAML source definition for this processor
   */
  protected abstract fun getYamlPropertySource(): YamlPropertySource
}

/**
 * Identifies a YAML property source and the classpath resource from which to load it.
 *
 * @property name a stable, application-unique source name; it is also used in the enablement property key
 * @property resourceLocation the YAML resource path relative to the classpath, for example
 * `META-INF/my-library-defaults.yaml`
 * @property order the environment post-processor order; lower values run first and therefore take precedence over
 * later YAML sources added by this base class
 *
 * This Kotlin data class is emitted as a Java record with a public
 * `YamlPropertySource(String name, String resourceLocation, int order)` canonical constructor and a
 * `YamlPropertySource(String name, String resourceLocation)` convenience constructor. The convenience constructor
 * uses [Ordered.LOWEST_PRECEDENCE]. Java consumers access the record components through `name()`,
 * `resourceLocation()`, and `order()`.
 */
@JvmRecord
data class YamlPropertySource(
  val name: String,
  val resourceLocation: String,
  val order: Int
) {

  /**
   * Creates a low-precedence YAML source.
   *
   * This overload provides the default order for both Kotlin and Java callers.
   */
  constructor(name: String, resourceLocation: String) : this(name, resourceLocation, Ordered.LOWEST_PRECEDENCE)
}
