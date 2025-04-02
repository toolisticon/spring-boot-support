package io.toolisticon.spring.testing.data

import java.util.*
import java.util.function.Consumer
import java.util.function.Function

typealias Version = Long

fun interface IdGenerator<ID> : Iterator<ID> {
  override fun hasNext(): Boolean = true
  override fun forEachRemaining(action: Consumer<in ID>) = throw UnsupportedOperationException("IdGenerator is infinite.")
}

fun interface IdExtractor<T : Any, ID : Any> : Function<T, Optional<ID>>
fun interface VersionExtractor<T : Any> : Function<T, Optional<Version>>
fun interface PrepareForSave<T : Any, ID : Any> : (T, ID, Version?) -> T


sealed interface EntityProperty<T : Any, ID : Any> {
  fun getId(entity: T): Optional<ID>

  fun setId(entity: T, id: ID): T
}

interface EntityIdProperty<T : Any, ID : Any> : EntityProperty<T, ID>

interface EntityVersionProperty<T : Any, ID : Any> : EntityIdProperty<T, ID> {
  fun getVersion(entity: T): Optional<Version>

  fun setVersion(entity: T, version: Version): T
}

fun <T : Any, ID : Any> EntityProperty<T, ID>.prepareSave(entity: T, idGenerator: IdGenerator<ID>): ID {
  this.getId(entity).ifPresentOrElse(
    {},
    { this.setId(entity, idGenerator.next()) }
  )

  if (this is EntityVersionProperty<T, ID>) {
    this.getVersion(entity).ifPresentOrElse(
      { this.setVersion(entity, it + 1) },
      { this.setVersion(entity, 0L) }
    )
  }
  return getId(entity).orElseThrow()
}
