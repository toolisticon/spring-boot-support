# Spring Boot Support

A collection of Spring Boot support tools.

[![incubating](https://img.shields.io/badge/lifecycle-INCUBATING-orange.svg)](https://github.com/holisticon#open-source-lifecycle)
[![Build Status](https://github.com/toolisticon/spring-conditions/workflows/Development%20branches/badge.svg)](https://github.com/toolisticon/spring-conditions/actions)
[![sponsored](https://img.shields.io/badge/sponsoredBy-Holisticon-RED.svg)](https://holisticon.de/)
[![Spring Boot 4.1.2 compatible](https://img.shields.io/badge/Spring%20Boot-4.1.2%20compatible-6DB33F?logo=springboot&logoColor=white)](https://docs.spring.io/spring-boot/4.1/)
[![Maven Central](https://img.shields.io/maven-central/v/io.toolisticon.spring/spring-boot-support-bom.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.toolisticon.spring/spring-boot-support-bom)

## Import

Import the BOM into your Maven project.

```xml 
<dependency>
  <groupId>io.toolisticon.spring</groupId>
  <artifactId>spring-boot-support-bom</artifactId>
  <version>1.0.1</version>
  <scope>import</scope>
  <type>pom</type>
</dependency>
```

## Spring Boot Conditions

Provides useful Spring Boot conditions.

### Supported conditions

- `@ConditionalOnMissingQualifiedBean`
- `@ConditionalOnPropertyExcept`
- `@ConditionalOnPropertyValues`

### Usage

```xml

<dependency>
  <groupId>io.toolisticon.spring</groupId>
  <artifactId>spring-boot-conditions</artifactId>
</dependency>
```

Use `@ConditionalOnPropertyExcept` when a configuration should apply unless a property has one of the excluded
values. As with Spring Boot's `@ConditionalOnProperty`, every named property must be present unless
`matchIfMissing` is enabled.

```kotlin
@Configuration
@ConditionalOnPropertyExcept(
  prefix = "example.feature",
  name = ["mode"],
  havingValue = ["disabled", "maintenance"]
)
class FeatureConfiguration
```

Use `@ConditionalOnPropertyValues` when a configuration should apply only if a property has one of the provided
values. As with Spring Boot's `@ConditionalOnProperty`, every named property must be present unless
`matchIfMissing` is enabled.

```kotlin
@Configuration
@ConditionalOnPropertyValues(
  prefix = "example.feature",
  name = ["mode"],
  havingValue = ["enabled", "maintenance"]
)
class FeatureConfiguration
```

## Spring Boot Properties

The `spring-boot-properties` module supplies two complementary ways for libraries and starters to provide YAML
defaults. In both approaches, normal application configuration has higher precedence and can override the defaults.

```xml
<dependency>
  <groupId>io.toolisticon.spring</groupId>
  <artifactId>spring-boot-properties</artifactId>
</dependency>
```

Choose `YamlPropertySourceFactory` when defaults are needed only by a specific configuration class. Choose
`AbstractYamlSourceFactoryEnvironmentPostProcessor` when defaults must influence auto-configuration conditions or
the default components created by auto-configuration.

### Approach 1: Configuration-local defaults with `YamlPropertySourceFactory`

Use this explicit, configuration-local approach when the defaults do not need to participate in decisions made before
the configuration class is processed. The consuming configuration imports its YAML resource using `@PropertySource`.

Define the configuration properties:

```kotlin
@ConfigurationProperties("myprops")
data class MyProperties(
  val foo: String,
  val zee: Int,
  val baz: Boolean
)

```

Define the configuration:

```kotlin

@EnableConfigurationProperties(MyProperties::class)
@PropertySource(
  name = "myDefaultProperties",
  value = ["classpath:/application-my-default.yaml"],
  factory = YamlPropertySourceFactory::class
)
class MyConfiguration

```

Place an `application-my-default.yaml` file in `src/main/resources`:

```yaml
myprops:
  foo: "bar"
  zee: 42
  baz: true
```

### Approach 2: Early defaults with `AbstractYamlSourceFactoryEnvironmentPostProcessor`

Use an environment post-processor when a default must be available before auto-configuration is evaluated. This is
important when the value affects an `@ConditionalOnProperty` decision or configuration of a default component.

Create a processor in your library. Omitting `order` uses `Ordered.LOWEST_PRECEDENCE`, so the defaults are installed
after Spring Boot has loaded normal configuration data. When multiple processors supply the same property, use
distinct order values: lower values run first and take precedence over later YAML defaults.

```kotlin
package com.example.mystarter

import io.toolisticon.spring.properties.AbstractYamlSourceFactoryEnvironmentPostProcessor
import io.toolisticon.spring.properties.YamlPropertySource

class MyDefaultsEnvironmentPostProcessor : AbstractYamlSourceFactoryEnvironmentPostProcessor() {

  protected override fun getYamlPropertySource(): YamlPropertySource = YamlPropertySource(
    name = "myStarterDefaults",
    resourceLocation = "META-INF/my-starter-defaults.yaml"
  )
}
```

To override the default order, provide it as the third `YamlPropertySource` argument:

```kotlin
protected override fun getYamlPropertySource(): YamlPropertySource = YamlPropertySource(
  name = "myStarterDefaults",
  resourceLocation = "META-INF/my-starter-defaults.yaml",
  order = 100
)
```

Use this only when the source competes with another YAML default source. A lower order value is added first and has
higher precedence; application-supplied configuration continues to override both sources.

The same extension point is available from Java:

```java
package com.example.mystarter;

import io.toolisticon.spring.properties.AbstractYamlSourceFactoryEnvironmentPostProcessor;
import io.toolisticon.spring.properties.YamlPropertySource;

public final class MyDefaultsEnvironmentPostProcessor
    extends AbstractYamlSourceFactoryEnvironmentPostProcessor {

  @Override
  protected YamlPropertySource getYamlPropertySource() {
    return new YamlPropertySource("myStarterDefaults", "META-INF/my-starter-defaults.yaml");
  }
}
```

Java consumers can likewise pass the order as the third constructor argument:
`new YamlPropertySource("myStarterDefaults", "META-INF/my-starter-defaults.yaml", 100)`.

Register the processor in `src/main/resources/META-INF/spring.factories` so that Spring Boot can discover it:

```properties
org.springframework.boot.env.EnvironmentPostProcessor=\
com.example.mystarter.MyDefaultsEnvironmentPostProcessor
```

Then place the defaults at `src/main/resources/META-INF/my-starter-defaults.yaml`:

```yaml
my-starter:
  feature-enabled: true
```

The source is enabled by default. Applications can opt out without removing the resource:

```properties
io.toolisticon.spring.properties.myStarterDefaults.enabled=false
```

The processor loads the YAML during environment preparation and appends it as a low-precedence property source.
Application properties, environment variables, command-line arguments, and other higher-precedence sources therefore
continue to override the library defaults.


# License

This library is published under the Apache License 2.0.
