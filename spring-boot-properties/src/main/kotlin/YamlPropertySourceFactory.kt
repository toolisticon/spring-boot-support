package io.toolisticon.spring.properties

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.PropertySourceFactory
import org.springframework.lang.NonNull

/**
 * Creates Spring property sources from YAML resources declared with `@PropertySource`.
 *
 * The factory converts the YAML document into [java.util.Properties] through [YamlPropertiesFactoryBean] and exposes
 * it as a [PropertiesPropertySource]. It uses the explicit `@PropertySource` name when present; otherwise, it uses
 * the resource filename. Both values are required because Spring property sources must have a name and properties.
 *
 * Inspired by [spring-yaml-propertysource](https://www.baeldung.com/spring-yaml-propertysource).
 */
class YamlPropertySourceFactory : PropertySourceFactory {

  override fun createPropertySource(name: String?, encodedResource: EncodedResource): PropertySource<*> {

    val factory = YamlPropertiesFactoryBean()

    factory.setResources(encodedResource.resource)
    val properties = factory.getObject()

    return PropertiesPropertySource(
      requireNotNull(name ?: encodedResource.resource.filename),
      requireNotNull(properties)
    )
  }
}
