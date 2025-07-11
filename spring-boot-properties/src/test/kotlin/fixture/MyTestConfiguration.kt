package io.toolisticon.spring.properties.fixture

import io.toolisticon.spring.properties.YamlPropertySourceFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@PropertySource(
  name = "defaultTestingProperties",
  value = ["classpath:/application-testing.yaml"],
  factory = YamlPropertySourceFactory::class
)
@EnableConfigurationProperties(MyProperties::class)
@Configuration
@ComponentScan
class MyTestConfiguration
