package io.toolisticon.spring.condition

import org.springframework.context.annotation.Conditional
import org.springframework.core.annotation.AliasFor

/**
 * Matches when every named property is present and its value is not one of [havingValue].
 *
 * Set [matchIfMissing] to `true` to also match missing properties. [value] is an alias for [name].
 */
@Target(
  AnnotationTarget.ANNOTATION_CLASS,
  AnnotationTarget.CLASS,
  AnnotationTarget.FUNCTION,
  AnnotationTarget.PROPERTY_GETTER,
  AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Conditional(OnPropertyExceptCondition::class)
annotation class ConditionalOnPropertyExcept(
  /** Prefix applied to every property [name]. */
  val prefix: String = "",
  /** Alias for [name]. */
  @get:AliasFor("name")
  val value: Array<String> = [],
  /** Names of the properties to check. */
  @get:AliasFor("value")
  val name: Array<String> = [],
  /** Values that prevent the condition from matching. */
  val havingValue: Array<String>,
  /** Whether a missing property should match. */
  val matchIfMissing: Boolean = false
)
