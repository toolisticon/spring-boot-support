package io.toolisticon.spring.properties

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.PropertySourceFactory
import org.springframework.lang.NonNull
import java.util.*

/**
 * Property source factory to read properties from YAML files.
 * Inspired by [spring-yaml-propertysource](https://www.baeldung.com/spring-yaml-propertysource).
 */
class YamlPropertySourceFactory : PropertySourceFactory {

  @NonNull
  override fun createPropertySource(name: String?, encodedResource: EncodedResource): PropertySource<*> {

    val factory = YamlPropertiesFactoryBean()

    factory.setResources(encodedResource.resource)
    val properties = factory.getObject()

    return PropertiesPropertySource(
      Objects.requireNonNull(name ?: encodedResource.resource.filename),
      Objects.requireNonNull(properties)
    )
  }
}
