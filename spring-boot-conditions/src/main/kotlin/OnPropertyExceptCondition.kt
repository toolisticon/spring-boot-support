package io.toolisticon.spring.condition

import org.springframework.boot.autoconfigure.condition.ConditionOutcome
import org.springframework.boot.autoconfigure.condition.SpringBootCondition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata

/** Evaluates [ConditionalOnPropertyExcept]. */
class OnPropertyExceptCondition : SpringBootCondition() {

  override fun getMatchOutcome(context: ConditionContext, metadata: AnnotatedTypeMetadata): ConditionOutcome {
    val attributes = metadata.getAnnotationAttributes(ConditionalOnPropertyExcept::class.java.name)!!
    val names = propertyNames(attributes)
    val prefix = (attributes["prefix"] as String).trimEnd('.')
    val excludedValues = stringArray(attributes["havingValue"])
    val matchIfMissing = attributes["matchIfMissing"] as Boolean

    if (names.isEmpty()) {
      return ConditionOutcome.noMatch("No property names specified")
    }

    for (name in names) {
      val propertyName = listOf(prefix, name).filter(String::isNotEmpty).joinToString(".")
      val propertyValue = context.environment.getProperty(propertyName)
      if (propertyValue == null) {
        if (!matchIfMissing) {
          return ConditionOutcome.noMatch("Property '$propertyName' is missing")
        }
      } else if (propertyValue in excludedValues) {
        return ConditionOutcome.noMatch("Property '$propertyName' has excluded value '$propertyValue'")
      }
    }

    return ConditionOutcome.match("All properties have non-excluded values")
  }

  private fun propertyNames(attributes: Map<String, Any?>): Array<String> {
    val names = stringArray(attributes["name"])
    return names.ifEmpty { stringArray(attributes["value"]) }
  }

  private fun stringArray(value: Any?): Array<String> =
    (value as Array<*>).map { it as String }.toTypedArray()
}
