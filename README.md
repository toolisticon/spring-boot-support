# Spring-Boot Support

A collection of Spring-Boot supporting tools.

[![incubating](https://img.shields.io/badge/lifecycle-INCUBATING-orange.svg)](https://github.com/holisticon#open-source-lifecycle)
[![Build Status](https://github.com/toolisticon/spring-conditions/workflows/Development%20branches/badge.svg)](https://github.com/toolisticon/spring-conditions/actions)
[![sponsored](https://img.shields.io/badge/sponsoredBy-Holisticon-RED.svg)](https://holisticon.de/)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.spring/spring-conditions/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.spring/spring-conditions)

## Import

Import the BOM into your Maven project.

```xml 
<dependency>
  <groupId>io.toolisticon.spring</groupId>
  <artifactId>spring-boot-bom</artifactId>
  <version>1.0.1</version>
  <scope>import</scope>
  <type>pom</type>
</dependency>
```

## Spring-Boot Conditions

Provides useful Spring-Boot conditions.

### Supported conditions

- `@ConditionalOnMissingQualifiedBean`

### Usage

```xml

<dependency>
  <groupId>io.toolisticon.spring</groupId>
  <artifactId>spring-boot-conditions</artifactId>
</dependency>
```

## YAML property source factory

Allows to define default properties as part of the starter using supplied YAML file.

### Usage

```xml
<dependency>
  <groupId>io.toolisticon.spring</groupId>
  <artifactId>spring-boot-properties</artifactId>
</dependency>
```

Define properties

```kotlin
@ConfigurationProperties("myprops")
data class MyProperties(
  val foo: String,
  val zee: Int,
  val baz: Boolean
)

```

Define configuration:

```kotlin

@EnableConfigurationProperties(MyProperties::class)
@PropertySource(
  name = "myDefaultProperties",
  value = ["classpath:/application-my-default.yaml"],
  factory = YamlPropertySourceFactory::class
)
class MyConfiguration 

```

Put a `application-my-default.yaml` into `src/main/resources`:

```yaml
myprops:
  foo: "bar"
  zee: 42
  baz: true
```


# License

This library is published under the Apache 2.0 license.
